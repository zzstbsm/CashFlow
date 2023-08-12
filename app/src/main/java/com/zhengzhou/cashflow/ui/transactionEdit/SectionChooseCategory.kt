package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.customUiElements.SelectIconsGrid
import com.zhengzhou.cashflow.data.Category
import java.util.UUID

@Composable
fun ChooseCategorySection(
    categoryList: List<Category>,
    currentlyChosenCategoryId: UUID,
    onCategoryChoice: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {

    val buttonModifier = Modifier
        .fillMaxWidth(.28f)
        .height(64.dp)
        .padding(2.dp)

    SelectIconsGrid(
        gridColumns = 3,
        categoryList = categoryList,
        currentlyChosenCategoryId = currentlyChosenCategoryId,
        onCategoryChoice = onCategoryChoice,
        modifier = modifier.padding(8.dp),
        buttonModifier = buttonModifier,
    )
}
