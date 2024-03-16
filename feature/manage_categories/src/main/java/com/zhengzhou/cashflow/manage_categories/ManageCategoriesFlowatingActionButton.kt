package com.zhengzhou.cashflow.manage_categories

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.manage_categories.view_model.ManageCategoriesUiState
import com.zhengzhou.cashflow.manage_categories.view_model.ManageCategoriesViewModel
import com.zhengzhou.cashflow.themes.icons.IconsMappedForDB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ManageCategoriesFloatingActionButton(
    manageCategoriesUiState: ManageCategoriesUiState,
    manageCategoriesViewModel: ManageCategoriesViewModel,
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    FloatingActionButton(
        onClick = {
            showDialog = true
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = stringResource(id = R.string.accessibility_add_category)
        )
    }

    if (showDialog) {
        BasicAlertDialog(onDismissRequest = { showDialog = false }
        ) {
            Card {
                val defaultNewCategoryName =
                    stringResource(id = R.string.new_category)
                var newCategoryName by remember {
                    mutableStateOf(defaultNewCategoryName)
                }
                var newCategoryIcon by remember {
                    mutableStateOf(IconsMappedForDB.HOME)
                }

                EditCategoryDetailsSection(
                    category = Category.newEmpty(),
                    newCategoryName = newCategoryName,
                    onEditCategoryName = {
                        newCategoryName = it
                    },
                    newCategoryIcon = newCategoryIcon,
                    onEditCategoryIcon = {
                        newCategoryIcon = it
                    },
                    horizontalPadding = 8.dp,
                    onDeleteCategory = {
                        showDialog = false
                    },
                    onSaveNewCategory = {
                        manageCategoriesViewModel.createCategory(
                            name = newCategoryName,
                            iconName = newCategoryIcon,
                            transactionType = manageCategoriesUiState.transactionType
                        )
                        showDialog = false
                    },
                    onUndoChanges = {
                        newCategoryIcon = IconsMappedForDB.HOME
                        newCategoryName = defaultNewCategoryName
                    },
                )
            }
        }
    }
}