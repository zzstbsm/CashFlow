package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.tools.mapIconsFromName
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

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(8.dp)
    ) {

        items(categoryList.size) {position ->

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

@Composable
private fun ChooseCategoryButton(
    category: Category
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = mapIconsFromName[category.iconName]!!),
            contentDescription = null, // TODO: add content description
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )
    }
}