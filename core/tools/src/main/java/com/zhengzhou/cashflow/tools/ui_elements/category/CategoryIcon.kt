package com.zhengzhou.cashflow.tools.ui_elements.category

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.zhengzhou.cashflow.themes.icons.IconsMappedForDB

@Composable
fun CategoryIcon(
    iconName: IconsMappedForDB,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = iconName.resource),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}