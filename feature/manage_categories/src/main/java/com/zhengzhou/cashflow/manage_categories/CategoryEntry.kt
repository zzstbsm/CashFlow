package com.zhengzhou.cashflow.manage_categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.themes.icons.IconsMappedForDB
import com.zhengzhou.cashflow.tools.ui_elements.category.CategoryIcon

@Composable
internal fun CategoryEntry(
    category: Category,
    categoryOccurrences: Int,
    onOpenCategory: (Category?) -> Unit,
    ifOpen: Boolean,
    onDeleteCategory: (Category) -> Unit,
    onSaveNewCategory: (Category) -> Unit,
) {

    // This variable is used to trigger a recomposition after the change of the order of the categories after editing the name of one
    var changeCategoryHandler by remember {
        mutableStateOf(category)
    }
    // Temporary category name to display on the screen, it is loaded in the db if the user saves the new name
    var newCategoryName by remember {
        mutableStateOf(category.name)
    }
    // Temporary category icon to display on the screen, it is loaded in the db if the user saves the new icon
    var newCategoryIcon by remember {
        mutableStateOf(category.iconName)
    }

    if (changeCategoryHandler != category) {
        changeCategoryHandler = category
        newCategoryName = category.name
        newCategoryIcon = category.iconName
    }

    Card(
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            ),
    ) {
        val horizontalPadding = 16.dp
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
        ) {
            CategoryDescription(
                ifOpen = ifOpen,
                category = category,
                onOpenCategory = onOpenCategory,
                modifier = Modifier.weight(9f)
            )
            CategoryOpenSectionArrow(
                ifOpen = ifOpen,
                category = category,
                onOpenCategory = onOpenCategory,
                onSetNewCategory = { categoryName, iconName ->
                    newCategoryName = categoryName
                    newCategoryIcon = iconName
                },
                modifier = Modifier.weight(1f)
            )

        }

        if (ifOpen) {

            CategoryOccurrences(
                categoryOccurrences = categoryOccurrences
            )

            EditCategoryDetailsSection(
                category = category,
                newCategoryName = newCategoryName,
                onEditCategoryName = {
                    newCategoryName = it
                },
                newCategoryIcon = newCategoryIcon,
                onEditCategoryIcon = {
                    newCategoryIcon = it
                },
                horizontalPadding = horizontalPadding,
                onDeleteCategory = onDeleteCategory,
                onSaveNewCategory = onSaveNewCategory,
            ) {

            }
        }
    }
}

@Composable
private fun CategoryDescription(
    ifOpen: Boolean,
    category: Category,
    onOpenCategory: (Category?) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable {
            val toReturnCategory = if (ifOpen) null else category
            onOpenCategory(toReturnCategory)
        }
    ) {
        CategoryIcon(
            iconName = category.iconName,
            contentDescription = category.name,
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
        )
        Text(
            text = category.name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CategoryOpenSectionArrow(
    ifOpen: Boolean,
    category: Category,
    onOpenCategory: (Category?) -> Unit,
    onSetNewCategory: (String, IconsMappedForDB) -> Unit,
    modifier: Modifier = Modifier
) {

    // Close and open category edit part
    val icon = if (ifOpen) painterResource(id = R.drawable.ic_up) else painterResource(id = R.drawable.ic_down)
    val contentDescription = if (ifOpen) stringResource(id = R.string.accessibility_close_category) else stringResource(id = R.string.accessibility_open_category)
    IconButton(
        onClick = {
            val toReturnCategory = if (ifOpen) null else category
            onOpenCategory(toReturnCategory)
            toReturnCategory?.let {
                onSetNewCategory(it.name,it.iconName)
            }
        },
        modifier = modifier
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun CategoryOccurrences(
    categoryOccurrences: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        HorizontalDivider()
        Text(
            text = stringResource(id = R.string.occurrences) + ": $categoryOccurrences",
        )
        HorizontalDivider()
    }
}