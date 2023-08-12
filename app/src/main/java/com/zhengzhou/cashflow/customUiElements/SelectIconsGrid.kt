package com.zhengzhou.cashflow.customUiElements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.data.Category
import java.util.UUID

@Composable
fun SelectIconsGrid(
    gridColumns: Int,
    categoryList: List<Category>,
    currentlyChosenCategoryId: UUID,
    onCategoryChoice: (Category) -> Unit,
    modifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns),
        modifier = modifier
    ) {

        items(categoryList.size) { position ->

            val itemCategory = categoryList[position]

            if (currentlyChosenCategoryId != itemCategory.id) {
                OutlinedButton(
                    onClick = {
                        onCategoryChoice(itemCategory)
                    },
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp),
                ) {
                    ChooseCategoryButton(
                        category = itemCategory
                    )
                }
            } else {
                Button(
                    onClick = { },
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp),
                    enabled = false,
                ) {
                    ChooseCategoryButton(
                        category = itemCategory
                    )
                }
            }
        }
    }
}