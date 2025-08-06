package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dellapp.weatherapp.core.common.BottomBarHeight
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.XXLargeSpacing
import org.jetbrains.compose.resources.stringResource
import weatherapp.composeapp.generated.addBottomDark
import weatherapp.composeapp.generated.addBottomLight
import weatherapp.composeapp.generated.bottomBarDark
import weatherapp.composeapp.generated.bottomBarLight
import weatherapp.composeapp.generated.icPin
import weatherapp.composeapp.generated.icSettings
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.current_position
import weatherapp.composeapp.generated.resources.settings

@Composable
fun BottomBar(
    modifier: Modifier,
    isDarkTheme: Boolean,
    onAddClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPositionClick: () -> Unit = {},
) {
    Box(modifier = modifier.height(BottomBarHeight)) {
        SvgImage(
            image = if (isDarkTheme) bottomBarDark else bottomBarLight,
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            contentScale = ContentScale.FillBounds,
        )
        SvgImage(
            image = if (isDarkTheme) addBottomDark else addBottomLight,
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxHeight().clickable {
                onAddClick()
            },
        )
        SvgImage(
            image = icSettings,
            contentDescription = stringResource(Res.string.settings),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier.align(Alignment.CenterEnd)
                .padding(end = XXLargeSpacing, top = LargeSpacing).size(36.dp).clickable {
                    onSettingsClick()
                },
        )
        SvgImage(
            image = icPin,
            contentDescription = stringResource(Res.string.current_position),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier.align(Alignment.CenterStart)
                .padding(start = XXLargeSpacing, top = LargeSpacing).size(36.dp).clickable {
                    onPositionClick()
                },
        )
    }
}