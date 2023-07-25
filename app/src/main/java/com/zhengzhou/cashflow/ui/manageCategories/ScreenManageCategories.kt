package com.zhengzhou.cashflow.ui.manageCategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.listCategoriesIconsId
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar

@Composable
fun ManageCategoriesScreen(
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController
) {

    val manageCategoriesViewModel: ManageCategoriesViewModel = viewModel {
        ManageCategoriesViewModel()
    }
    val manageCategoriesUiState: ManageCategoriesUiState by manageCategoriesViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            SectionNavigationDrawerSheet(
                drawerState = drawerState,
                currentScreen = currentScreen,
                setCurrentScreen = setCurrentScreen,
                navController = navController,
            )
        },
        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
        drawerState = drawerState,
    ) {
        Scaffold(
            topBar = {
                SectionTopAppBar(
                    currentScreen = currentScreen,
                    drawerState = drawerState,
                    actions = {
                        SectionTopAppBar(
                            currentScreen = currentScreen,
                            drawerState = drawerState,
                        )
                    }
                )
            },
            content = { innerPadding ->
                ManageCategoriesMainBody(
                    manageCategoriesUiState = manageCategoriesUiState,
                    manageCategoriesViewModel = manageCategoriesViewModel,
                    innerPadding = innerPadding,
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    setCurrentScreen = setCurrentScreen,
                    navController = navController
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val transactionType = manageCategoriesUiState.transactionType
                        manageCategoriesViewModel.createCategory(transactionType)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = stringResource(id = R.string.accessibility_add_category)
                    )
                }
            }
        )
    }
}

@Composable
fun ManageCategoriesMainBody(
    manageCategoriesUiState: ManageCategoriesUiState,
    manageCategoriesViewModel: ManageCategoriesViewModel,
    innerPadding: PaddingValues,
) {
    var activeTab by remember {
        mutableIntStateOf(0)
    }

    val manageCategoriesScreenToChooseFunctionality = listOf(
        TransactionType.Expense,
        TransactionType.Deposit,
        TransactionType.Move,
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(innerPadding),
    ) {
        TabRow(selectedTabIndex = activeTab) {
            manageCategoriesScreenToChooseFunctionality.forEachIndexed { index, transactionType ->
                Tab(
                    selected = activeTab == index,
                    onClick = {
                        activeTab = index
                        manageCategoriesViewModel.setTransactionType(transactionType)
                    },
                    text = { Text(text = stringResource(id = transactionType.text)) },
                    icon = {
                        Icon(
                            painter = painterResource(id = transactionType.iconId),
                            contentDescription = stringResource(id = transactionType.text)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn {
            val listCategories = manageCategoriesUiState.listCategories.filter { category ->
                category.transactionTypeId == manageCategoriesUiState.transactionType.id
            }
            items(listCategories.size) { position ->
                val category = listCategories[position]
                val categoryOccurrences = manageCategoriesUiState.openCategoryOccurrences
                val ifOpenCategory = category == manageCategoriesUiState.openCategory
                CategoryEntry(
                    category = category,
                    categoryOccurrences = categoryOccurrences,
                    onOpenCategory = { toOpenCategory ->
                        if (ifOpenCategory) manageCategoriesViewModel.setOpenCategory(null)
                        else manageCategoriesViewModel.setOpenCategory(toOpenCategory)
                    },
                    ifOpen = ifOpenCategory,
                    onDeleteCategory = { categoryToDelete: Category ->
                        manageCategoriesViewModel.deleteCategory(category = categoryToDelete)
                    },
                    onSaveNewCategory = { categoryToSave: Category ->
                        manageCategoriesViewModel.editCategory(category = categoryToSave)
                    }
                )
            }
        }

    }

}

@Composable
private fun CategoryEntry(
    category: Category,
    categoryOccurrences: Int,
    onOpenCategory: (Category?) -> Unit,
    ifOpen: Boolean,
    onDeleteCategory: (Category) -> Unit,
    onSaveNewCategory: (Category) -> Unit,
) {

    var newCategoryName by remember {
        mutableStateOf(category.name)
    }
    var newCategoryIcon by remember {
        mutableIntStateOf(category.idIcon)
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = category.idIcon),
                    contentDescription = category.name,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp)
                )
                Text(text = category.name)
            }

            // Close and open category edit part
            val icon = if (ifOpen) painterResource(id = R.drawable.ic_up) else painterResource(id = R.drawable.ic_down)
            val contentDescription = if (ifOpen) stringResource(id = R.string.accessibility_close_category) else stringResource(id = R.string.accessibility_open_category)
            IconButton(
                onClick = {
                    val toReturnCategory = if (ifOpen) null else category
                    onOpenCategory(toReturnCategory)
                    if (toReturnCategory != null) {
                        newCategoryIcon = toReturnCategory.idIcon
                        newCategoryName = toReturnCategory.name
                    }
                }
            ) {
                Icon(
                    painter = icon,
                    contentDescription = contentDescription,
                )
            }
        }

        if (ifOpen) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(
                    horizontal = horizontalPadding,
                    vertical = 8.dp,
                )
            ) {
                Divider()
                Text(
                    text = stringResource(id = R.string.ManageCategories_occurrences) + ": $categoryOccurrences",
                    modifier = Modifier.padding(4.dp)
                )

                Divider()
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    ChooseCategoryIcon(
                        currentIcon = newCategoryIcon,
                        onChooseIcon ={ categoryIcon ->
                            newCategoryIcon = categoryIcon
                        } ,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    )
                    ChooseCategoryName(
                        categoryName = newCategoryName,
                        onCategoryNameChange = {
                            newCategoryName = it
                        },
                        modifier = Modifier
                            .weight(3f)
                            .padding(4.dp)
                    )
                }

                ActionButtons(
                    onDeleteClick = {
                        onDeleteCategory(category)
                    },
                    onUndoClick = {
                        newCategoryName = category.name
                        newCategoryIcon = category.idIcon
                    },
                    onSaveClick = {
                        val newCategory = category.copy(
                            name = newCategoryName,
                            idIcon = newCategoryIcon,
                        )
                        onSaveNewCategory(newCategory)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChooseCategoryIcon(
    currentIcon: Int,
    onChooseIcon: (Int) -> Unit,
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
        Icon(
            painter = painterResource(id = currentIcon),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
    
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.ManageCategories_choose_category_icon),
                    modifier = Modifier.padding(8.dp)
                )

                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    items(listCategoriesIconsId.size) { position ->
                        val currentIconInGrid = listCategoriesIconsId[position]
                        OutlinedButton(
                            enabled = currentIconInGrid != currentIcon,
                            onClick = {
                                onChooseIcon(currentIconInGrid)
                                showDialog = false
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = currentIconInGrid),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
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