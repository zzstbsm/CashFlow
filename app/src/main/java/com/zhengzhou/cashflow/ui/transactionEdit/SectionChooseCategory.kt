package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.ui.SelectIconsGrid
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
        modifier = Modifier.padding(8.dp),
        buttonModifier = buttonModifier,
    )
}
