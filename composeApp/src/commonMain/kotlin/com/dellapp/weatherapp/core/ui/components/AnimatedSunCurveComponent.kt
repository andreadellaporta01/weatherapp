import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.PI
import kotlin.math.sin
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun AnimatedSunCurveComponent(
    sunriseTime: LocalTime,
    sunsetTime: LocalTime,
    modifier: Modifier = Modifier
) {
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    val sunriseMinutes = sunriseTime.toSecondOfDay() / 60
    val sunsetMinutes = sunsetTime.toSecondOfDay() / 60
    val currentMinutes = currentTime.toSecondOfDay() / 60

    val (sunProgress, isNightTime) = calculateSunProgress(
        currentMinutes, sunriseMinutes, sunsetMinutes
    )

    val animatedProgress by animateFloatAsState(
        targetValue = sunProgress,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawAnimatedSunCurve(
                animatedProgress = animatedProgress,
                isNightTime = isNightTime
            )
        }
    }
}

private fun calculateSunProgress(
    currentMinutes: Int,
    sunriseMinutes: Int,
    sunsetMinutes: Int
): Pair<Float, Boolean> {
    return when {
        currentMinutes in sunriseMinutes..sunsetMinutes -> {
            val dayLength = sunsetMinutes - sunriseMinutes
            val dayProgress = (currentMinutes - sunriseMinutes).toFloat() / dayLength
            Pair(dayProgress, false)
        }
        currentMinutes > sunsetMinutes -> {
            val nightLength = (24 * 60) - sunsetMinutes + sunriseMinutes
            val nightProgress = (currentMinutes - sunsetMinutes).toFloat() / nightLength
            Pair(nightProgress, true)
        }
        currentMinutes < sunriseMinutes -> {
            val nightLength = (24 * 60) - sunsetMinutes + sunriseMinutes
            val nightProgress = ((24 * 60) - sunsetMinutes + currentMinutes).toFloat() / nightLength
            Pair(nightProgress, true)
        }
        else -> Pair(0f, false)
    }
}

private fun DrawScope.drawAnimatedSunCurve(
    animatedProgress: Float,
    isNightTime: Boolean
) {
    val width = size.width
    val height = size.height
    val centerY = height / 2

    drawLine(
        color = Color.White.copy(alpha = 0.3f),
        start = Offset(0f, centerY),
        end = Offset(width, centerY),
        strokeWidth = 2.dp.toPx()
    )

    val dayCurve = Path().apply {
        val steps = 100
        for (i in 0..steps) {
            val pathProgress = i.toFloat() / steps
            val x = width * pathProgress
            val y = centerY - (sin(pathProgress * PI) * height * 0.3).toFloat()

            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
    }

    val nightCurve = Path().apply {
        val steps = 100
        for (i in 0..steps) {
            val pathProgress = i.toFloat() / steps
            val x = width * pathProgress
            val y = centerY + (sin(pathProgress * PI) * height * 0.2).toFloat()

            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
    }

    drawPath(
        path = dayCurve,
        color = Color(0xFF81C784),
        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
    )

    drawPath(
        path = nightCurve,
        color = Color(0xFF5C6BC0),
        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
    )

    val (sunX, sunY) = if (isNightTime) {
        val x = width * animatedProgress
        val y = centerY + (sin(animatedProgress * PI) * height * 0.2).toFloat()
        Pair(x, y)
    } else {
        val x = width * animatedProgress
        val y = centerY - (sin(animatedProgress * PI) * height * 0.3).toFloat()
        Pair(x, y)
    }

    val glowIntensity = if (isNightTime) 0.2f else sin(animatedProgress * PI).coerceAtLeast(0.3).toFloat()
    drawCircle(
        color = Color.White.copy(alpha = 0.3f * glowIntensity),
        radius = 20.dp.toPx() * glowIntensity,
        center = Offset(sunX, sunY)
    )

    val sunColor = when {
        isNightTime -> Color(0xFF9E9E9E)
        animatedProgress < 0.2f -> Color(0xFFFFA726)
        animatedProgress > 0.8f -> Color(0xFFFF7043)
        else -> Color.White
    }

    drawCircle(
        color = sunColor,
        radius = 12.dp.toPx(),
        center = Offset(sunX, sunY)
    )

    drawSunriseMarker(Offset(0f, centerY))
    drawSunsetMarker(Offset(width, centerY))
}

private fun DrawScope.drawSunriseMarker(position: Offset) {
    drawCircle(
        color = Color(0xFFFFA726),
        radius = 6.dp.toPx(),
        center = position
    )
}

private fun DrawScope.drawSunsetMarker(position: Offset) {
    drawCircle(
        color = Color(0xFFFF7043),
        radius = 6.dp.toPx(),
        center = position
    )
}