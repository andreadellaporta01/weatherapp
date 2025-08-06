package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dellapp.weatherapp.core.common.CardBorderColor
import com.dellapp.weatherapp.core.common.Shapes
import com.dellapp.weatherapp.core.common.SmallSpacing
import com.dellapp.weatherapp.core.common.TinySpacing
import com.dellapp.weatherapp.core.common.XLargeSpacing
import com.dellapp.weatherapp.core.common.mpsToKmh
import org.jetbrains.compose.resources.stringResource
import weatherapp.composeapp.generated.icWind
import weatherapp.composeapp.generated.icWindArrow
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.wind

@Composable
fun WindCard(
    windSpeed: Double,
    windAngle: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = Shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 2.dp,
            color = CardBorderColor.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(XLargeSpacing),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SvgImage(
                    image = icWind,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                    contentDescription = null
                )
                Spacer(Modifier.width(TinySpacing))
                Text(
                    text = stringResource(Res.string.wind),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                )
            }
            Spacer(Modifier.height(SmallSpacing))
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                SvgImage(
                    image = weatherapp.composeapp.generated.wind,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.size(120.dp),
                    contentDescription = null
                )
                Text(
                    text = "${mpsToKmh(windSpeed)}\nkm/h",
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )
                SvgImage(
                    image = icWindArrow,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(120.dp)
                        .rotate(windAngle.toFloat())
                )
            }
        }
    }

}