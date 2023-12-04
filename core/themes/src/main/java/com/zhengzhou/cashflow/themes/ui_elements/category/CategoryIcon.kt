package com.zhengzhou.cashflow.themes.ui_elements.category

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun CategoryIcon(
    iconName: com.zhengzhou.cashflow.themes.IconsMappedForDB,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = iconName.resource),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}