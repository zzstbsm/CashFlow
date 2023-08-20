package com.zhengzhou.cashflow.ui.manageCategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.customUiElements.CategoryIcon
import com.zhengzhou.cashflow.customUiElements.IconChoiceDialog
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.themes.IconsMappedForDB


@Composable
fun EditCategoryDetailsSection(
    category: Category,
    newCategoryName: String,
    onEditCategoryName: (String) ->Unit,
    newCategoryIcon: IconsMappedForDB,
    onEditCategoryIcon: (IconsMappedForDB) -> Unit,
    categoryOccurrences: Int,
    horizontalPadding: Dp,
    onDeleteCategory: (Category) -> Unit,
    onSaveNewCategory: (Category) -> Unit,
    onUndoChanges: () -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(
            horizontal = horizontalPadding,
            vertical = 8.dp,
        )
    ) {
        HorizontalDivider()
        Text(
            text = stringResource(id = R.string.ManageCategories_occurrences) + ": $categoryOccurrences",
            modifier = Modifier.padding(4.dp)
        )

        HorizontalDivider()
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            ChooseCategoryIcon(
                currentIcon = newCategoryIcon,
                onChooseIcon = onEditCategoryIcon,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
            ChooseCategoryName(
                categoryName = newCategoryName,
                onCategoryNameChange = onEditCategoryName,
                modifier = Modifier
                    .weight(3f)
                    .padding(4.dp)
            )
        }

        ActionButtons(
            onDeleteClick = {
                onDeleteCategory(category)
            },
            onUndoClick = onUndoChanges,
            onSaveClick = {
                val newCategory = category.copy(
                    name = newCategoryName,
                    iconName = newCategoryIcon,
                )
                onSaveNewCategory(newCategory)
            }
        )
    }
}

@Composable
private fun ChooseCategoryIcon(
    currentIcon: IconsMappedForDB,
    onChooseIcon: (IconsMappedForDB) -> Unit,
    modifier: Modifier = Modifier
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    OutlinedButton(
        onClick = { showDialog = true },
        shape = RoundedCornerShape(4.dp),
        modifier = modifier,
    ) {
        CategoryIcon(
            iconName = currentIcon,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }

    if (showDialog) {
        IconChoiceDialog(
            text = stringResource(id = R.string.ManageCategories_choose_category_icon),
            iconList = IconsMappedForDB.values().toList()
                .toList()
                .filter { it.category },
            onDismissRequest = { showDialog = false },
            currentSelectedIcon = currentIcon,
            onChooseIcon = onChooseIcon,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ChooseCategoryName(
    categoryName: String,
    onCategoryNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = categoryName,
        onValueChange = onCategoryNameChange,
        label = { Text(text = stringResource(R.string.name)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_description),
                contentDescription = null
            )
        },
        maxLines = 1,
        modifier = modifier
    )
}

@Composable
private fun ActionButtons(
    onDeleteClick: () -> Unit,
    onUndoClick: () -> Unit,
    onSaveClick: () -> Unit,
){
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onDeleteClick
        ) {
            Text(text = stringResource(id = R.string.delete))
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onUndoClick
            ) {
                Text(text = stringResource(id = R.string.undo))
            }

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = onSaveClick
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}