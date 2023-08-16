package com.zhengzhou.cashflow.customUiElements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconChoiceDialog(
    text: String,
    iconList: List<com.zhengzhou.cashflow.themes.IconsMappedForDB>,
    onDismissRequest: () -> Unit,
    currentSelectedIcon: com.zhengzhou.cashflow.themes.IconsMappedForDB?,
    onChooseIcon: (com.zhengzhou.cashflow.themes.IconsMappedForDB) -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(8.dp)
            )

            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(iconList.size) { position ->
                    val icon = iconList[position]
                    OutlinedButton(
                        enabled = icon != currentSelectedIcon,
                        onClick = {
                            onChooseIcon(icon)
                            onDismissRequest()
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .testTag(icon.name),
                    ) {
                        CategoryIcon(
                            iconName = icon,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}