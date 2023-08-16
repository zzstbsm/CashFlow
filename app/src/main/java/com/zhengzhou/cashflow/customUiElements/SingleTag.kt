package com.zhengzhou.cashflow.customUiElements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.zhengzhou.cashflow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleTag(
    tag: String = "",
    selected: Boolean,
    onTagClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (tag == "") {
            Text(
                text = stringResource(id = R.string.tag_no),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            FilterChip(
                selected = selected,
                onClick = { onTagClick() },
                label = { Text(text = tag) },
                leadingIcon = {
                    if (selected) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = null,
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = stringResource(R.string.delete_tag, tag),
                        )
                    }
                }
            )
        }
    }
}