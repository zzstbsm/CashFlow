package com.zhengzhou.cashflow.manage_categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.manage_categories.view_model.ManageCategoriesUiState
import com.zhengzhou.cashflow.manage_categories.view_model.ManageCategoriesViewModel
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.themes.ui_elements.navigation.BottomNavigationBar
import com.zhengzhou.cashflow.themes.ui_elements.navigation.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.themes.ui_elements.navigation.SectionTopAppBar

@Composable
fun ManageCategoriesScreen(
    repository: RepositoryInterface,
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    navController: NavController
) {

    val manageCategoriesViewModel: ManageCategoriesViewModel = viewModel {
        ManageCategoriesViewModel(
            repository = repository
        )
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
                ManageCategoriesFloatingActionButton(
                    manageCategoriesUiState = manageCategoriesUiState,
                    manageCategoriesViewModel = manageCategoriesViewModel,
                )
            }
        )
    }
}

@Composable
internal fun ManageCategoriesMainBody(
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
                category.transactionType == manageCategoriesUiState.transactionType
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
