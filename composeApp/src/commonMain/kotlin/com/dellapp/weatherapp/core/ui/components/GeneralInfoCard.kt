package com.dellapp.weatherapp.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dellapp.weatherapp.core.common.CardBorderColor
import com.dellapp.weatherapp.core.common.MediumSpacing
import com.dellapp.weatherapp.core.common.Shapes
import com.dellapp.weatherapp.core.common.TinySpacing
import com.dellapp.weatherapp.core.common.XLargeSpacing

@Composable
fun GeneralInfoCard(
    icon: String,
    name: String,
    title: String,
    description: String? = null,
    subtitle: String? = null,
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
            modifier = Modifier.fillMaxHeight().padding(XLargeSpacing),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SvgImage(
                    image = icon,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )
                Spacer(Modifier.width(TinySpacing))
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                )
            }
            Spacer(Modifier.height(MediumSpacing))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 32.sp)
            )
            if(subtitle != null) {
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 20.sp)
                )
            }
            if(description != null) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                )
            }
        }
    }

}