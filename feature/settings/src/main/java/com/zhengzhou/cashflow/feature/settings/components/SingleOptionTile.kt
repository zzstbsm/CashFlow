package com.zhengzhou.cashflow.feature.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun SingleOptionTile(
    title: String,
    description: String?,
    icon: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(onClick = onClick)
    ) {

        val leftSideModifier = Modifier
            .padding(start = 32.dp)
            .padding(4.dp)
            .size(24.dp)

        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = leftSideModifier
            )
        } else {
            Column(modifier = leftSideModifier) { }
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontStyle = FontStyle.Normal,
            )
            if (description != null) {
                Text(
                    text = description,
                    maxLines = 2,
                    minLines = 1,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}