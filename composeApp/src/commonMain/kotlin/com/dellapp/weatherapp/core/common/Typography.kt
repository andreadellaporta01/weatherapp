package com.dellapp.weatherapp.core.common

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.SF_Pro_Display_Regular
import weatherapp.composeapp.generated.resources.SF_Pro_Display_Semibold
import weatherapp.composeapp.generated.resources.SF_Pro_Display_Thin
import weatherapp.composeapp.generated.resources.SF_Pro_Text_Bold
import weatherapp.composeapp.generated.resources.SF_Pro_Text_Medium
import weatherapp.composeapp.generated.resources.SF_Pro_Text_Regular
import weatherapp.composeapp.generated.resources.SF_Pro_Text_Semibold

@Composable
fun appTypography(): Typography {
    val sfProText = FontFamily(
        Font(Res.font.SF_Pro_Text_Bold, FontWeight.Bold),
        Font(Res.font.SF_Pro_Text_Semibold, FontWeight.SemiBold),
        Font(Res.font.SF_Pro_Text_Regular, FontWeight.Normal),
        Font(Res.font.SF_Pro_Text_Medium, FontWeight.Medium),
    )

    val sfProDisplay = FontFamily(
        Font(Res.font.SF_Pro_Display_Regular, FontWeight.Normal),
        Font(Res.font.SF_Pro_Display_Thin, FontWeight.Thin),
        Font(Res.font.SF_Pro_Display_Semibold, FontWeight.SemiBold),
    )

    return Typography(
        headlineLarge = TextStyle(
            fontFamily = sfProDisplay,
            fontWeight = FontWeight.Normal,
            fontSize = 34.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = sfProText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = sfProText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        ),
        titleLarge = TextStyle(
            fontFamily = sfProDisplay,
            fontWeight = FontWeight.Thin,
            fontSize = 96.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = sfProText,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp
        ),
        labelLarge = TextStyle(
            fontFamily = sfProDisplay,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = sfProText,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )
    )
}