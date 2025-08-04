package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dellapp.weatherapp.core.common.BottomBarHeight
import com.dellapp.weatherapp.core.common.LargeSpacing
import com.dellapp.weatherapp.core.common.XXLargeSpacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import weatherapp.composeapp.generated.resources.Res
import weatherapp.composeapp.generated.resources.add_bottom
import weatherapp.composeapp.generated.resources.bottom_bar
import weatherapp.composeapp.generated.resources.current_position
import weatherapp.composeapp.generated.resources.ic_pin
import weatherapp.composeapp.generated.resources.ic_settings
import weatherapp.composeapp.generated.resources.settings

@Composable
fun BottomBar(
    modifier: Modifier,
    onAddClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPositionClick: () -> Unit = {}
) {
    Box(modifier = modifier.height(BottomBarHeight)) {
        Image(
            painter = painterResource(Res.drawable.bottom_bar),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).height(88.dp),
            contentScale = ContentScale.FillWidth
        )
        Image(
            painter = painterResource(Res.drawable.add_bottom),
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxHeight().clickable {
                onAddClick()
            },
            contentScale = ContentScale.FillHeight
        )
        Icon(
            painter = painterResource(Res.drawable.ic_settings),
            contentDescription = stringResource(Res.string.settings),
            tint = Color.White,
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                .padding(end = XXLargeSpacing, top = LargeSpacing).size(36.dp).clickable {
                    onSettingsClick()
                },
        )
        Icon(
            painter = painterResource(Res.drawable.ic_pin),
            contentDescription = stringResource(Res.string.current_position),
            tint = Color.White,
            modifier = Modifier.align(Alignment.CenterStart).fillMaxHeight()
                .padding(start = XXLargeSpacing, top = LargeSpacing).size(36.dp).clickable {
                    onPositionClick()
                },
        )
    }
}