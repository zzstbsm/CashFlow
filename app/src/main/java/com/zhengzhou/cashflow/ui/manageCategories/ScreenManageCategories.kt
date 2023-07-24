package com.zhengzhou.cashflow.ui.manageCategories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
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
                    navController = navController,
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
                    onClick = { /*TODO*/ }
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
    navController: NavController,
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

        LazyColumn {
            val listCategories = manageCategoriesUiState.listCategories.filter { category ->
                category.transactionTypeId == manageCategoriesUiState.transactionType.id
            }
            items(listCategories.size) { position ->
                val category = listCategories[position]
                val ifOpenCategory = category == manageCategoriesUiState.openCategory
                CategoryEntry(
                    category = category,
                    onOpenCategory = { toOpenCategory ->
                        if (ifOpenCategory) manageCategoriesViewModel.setOpenCategory(null)
                        else manageCategoriesViewModel.setOpenCategory(toOpenCategory)
                    },
                    ifOpen = ifOpenCategory,
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
    onOpenCategory: (Category?) -> Unit,
    ifOpen: Boolean,
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
        val padding = 16.dp
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = category.idIcon),
                    contentDescription = category.name,
                    modifier = Modifier.size(32.dp)
                )
                Text(text = category.name)
            }
            val icon = if (ifOpen) painterResource(id = R.drawable.ic_up) else painterResource(id = R.drawable.ic_down)
            val contentDescription = if (ifOpen) stringResource(id = R.string.accessibility_close_category) else stringResource(id = R.string.accessibility_open_category)
            IconButton(
                onClick = {
                    val toReturnCategory = if (ifOpen) null else category
                    onOpenCategory(toReturnCategory)
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
                modifier = Modifier.padding(padding)
            ) {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = {
                        newCategoryName = it
                    },
                    label = { Text(text = stringResource(R.string.name)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_description),
                            contentDescription = null
                        )
                    }
                )
                // TODO: add change category icon
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(.5f)
            ) {
                Button(
                    onClick = { 
                        newCategoryName = category.name
                        newCategoryIcon = category.idIcon
                    }
                ) {
                    Text(text = stringResource(id = R.string.undo))
                }
                Button(
                    onClick = {
                        val newCategory = category.copy(
                            name = newCategoryName,
                            idIcon = newCategoryIcon,
                        )
                        onSaveNewCategory(newCategory)
                    }
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    }
}