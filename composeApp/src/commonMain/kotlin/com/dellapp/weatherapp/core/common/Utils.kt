package com.dellapp.weatherapp.core.common

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun parseTimestampSecondsToLocalDateTime(
    timestampSeconds: Int,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): LocalDateTime {
    val instant = Instant.fromEpochSeconds(timestampSeconds.toLong())
    return instant.toLocalDateTime(timeZone)
}

fun LocalDateTime.formatToAmPm(): String {
    val hour = this.hour
    val amPm = if (hour < 12) "AM" else "PM"
    val formattedHour = when (hour % 12) {
        0 -> 12
        else -> hour % 12
    }
    return "$formattedHour $amPm"
}

fun DayOfWeek.localizedShortName(language: String): String {
    return when (language.lowercase()) {
        "en" -> when (this) {
            DayOfWeek.MONDAY -> "MON"
            DayOfWeek.TUESDAY -> "TUE"
            DayOfWeek.WEDNESDAY -> "WED"
            DayOfWeek.THURSDAY -> "THU"
            DayOfWeek.FRIDAY -> "FRI"
            DayOfWeek.SATURDAY -> "SAT"
            DayOfWeek.SUNDAY -> "SUN"
        }

        "it" -> when (this) {
            DayOfWeek.MONDAY -> "LUN"
            DayOfWeek.TUESDAY -> "MAR"
            DayOfWeek.WEDNESDAY -> "MER"
            DayOfWeek.THURSDAY -> "GIO"
            DayOfWeek.FRIDAY -> "VEN"
            DayOfWeek.SATURDAY -> "SAB"
            DayOfWeek.SUNDAY -> "DOM"
        }

        "es" -> when (this) {
            DayOfWeek.MONDAY -> "LUN"
            DayOfWeek.TUESDAY -> "MAR"
            DayOfWeek.WEDNESDAY -> "MIÉ"
            DayOfWeek.THURSDAY -> "JUE"
            DayOfWeek.FRIDAY -> "VIE"
            DayOfWeek.SATURDAY -> "SÁB"
            DayOfWeek.SUNDAY -> "DOM"
        }

        else -> this.name.take(3) // fallback (e.g., "MON")
    }
}

fun String.toLanguageCode(): String = when (this) {
    "English" -> "en"
    "Italiano" -> "it"
    "Español" -> "es"
    else -> "en" // fallback
}

