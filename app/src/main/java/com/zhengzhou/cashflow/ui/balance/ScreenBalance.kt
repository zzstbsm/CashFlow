package com.zhengzhou.cashflow.ui.balance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.customUiElements.BottomNavigationBar
import com.zhengzhou.cashflow.customUiElements.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.customUiElements.SectionTopAppBar
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.dataForUi.TransactionAndCategory
import com.zhengzhou.cashflow.navigation.NavigationCurrentScreen
import com.zhengzhou.cashflow.navigation.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.navigation.Screen

@Composable
fun BalanceScreen(
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController
) {

    val balanceViewModel: BalanceViewModel = viewModel {
        BalanceViewModel()
    }
    val balanceUiState by balanceViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.Balance.route,
        navController = navController,
    ) {
        setCurrentScreen(NavigationCurrentScreen.Balance)
    }

    ModalNavigationDrawer(
        drawerContent = {
            SectionNavigationDrawerSheet(
                drawerState = drawerState,
                currentScreen = currentScreen,
                setCurrentScreen = setCurrentScreen,
                navController = navController
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
                )
            },
            content = { innerPadding ->
                BalanceMainBody(
                    balanceUiState = balanceUiState,
                    balanceViewModel = balanceViewModel,
                    innerPaddingValues = innerPadding,
                    navController = navController,
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    setCurrentScreen = setCurrentScreen,
                    navController = navController,
                )
            },
            floatingActionButton = {
                if (!balanceUiState.isLoading) {
                    BalanceFloatingActionButtons(
                        wallet = balanceUiState.getLastWallet(),
                        transaction = Transaction.newEmpty(),
                        navController = navController,
                    )
                }
            },
        )
    }

}

@Composable
private fun BalanceMainBody(
    balanceUiState: BalanceUiState,
    balanceViewModel: BalanceViewModel,
    innerPaddingValues: PaddingValues,
    navController: NavController,
) {

    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp, vertical = 4.dp)
    val lazyGridSpan = 3
    var showTransactionListDialog by remember {
        mutableStateOf(false)
    }
    var transactionListForDialog by remember {
        mutableStateOf(listOf<TransactionAndCategory>())
    }

    val wallet = balanceUiState.equivalentWallet

    Column(
        modifier = Modifier.padding(innerPaddingValues)
    ) {
        CreditCardSection(
            balanceUiState = balanceUiState,
            balanceViewModel = balanceViewModel,
            modifier = modifier
        )
        BalanceTabOptionsSelector(
            selectedTab = balanceUiState.shownTab,
            onSelectTab = { balanceViewModel.selectTabToShow(it) }
        )

        LazyVerticalGrid(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Top,
            columns = GridCells.Fixed(lazyGridSpan),
        ) {

            item(span = { GridItemSpan(lazyGridSpan) }) {
                Column {
                    val timeFilterList = TimeFilterForSegmentedButton.values().toList()
                    SelectTimeFilter(
                        currentTimeFilter = balanceUiState.timeFilter,
                        timeFilterList = timeFilterList,
                        onSelectTimeFilter = { timeFilter ->
                            balanceViewModel.setTimeFilter(timeFilter)
                        },
                        modifier = modifier,
                    )
                    SelectedPeriodShow(
                        startDate = balanceUiState.filterStartDate,
                        endDate = balanceUiState.filterEndDate,
                        timeFilter = balanceUiState.timeFilter,
                        onSelectTimePeriod = { startDate, endDate ->
                            balanceViewModel.setTimeFilter(
                                timeFilter = balanceUiState.timeFilter,
                                startDate = startDate,
                                endDate = endDate,
                                navigation = true,
                            )
                        },
                        modifier = modifier,
                    )
                }
            }

            when (balanceUiState.shownTab) {

                BalanceTabOptions.CATEGORIES -> {
                    val transactionList =
                        balanceUiState.transactionListToShow.map { transactionAndCategory ->
                            transactionAndCategory.transaction
                        }
                    val categoryList = balanceUiState.categoryList

                    item(span = { GridItemSpan(lazyGridSpan) }) {
                        BalanceInSelectedPeriod(
                            currency = wallet.currency,
                            transactionList = transactionList,
                        )
                    }

                    items(categoryList.size) { position ->

                        val category = categoryList[position]
                        val filteredList = transactionList.filter {
                            it.categoryUUID == category.id
                        }
                        val existTransactionsInCurrentCategory = filteredList.isNotEmpty()
                        val amount = if (existTransactionsInCurrentCategory) {
                            filteredList.map { it.amount }.sum()
                        } else 0f

                        SectionCategoryItem(
                            amount = amount,
                            category = category,
                            currency = wallet.currency,
                            onClick = { selectedCategory ->
                                transactionListForDialog = filteredList.map {
                                    TransactionAndCategory(
                                        transaction = it,
                                        category = selectedCategory,
                                    )
                                }
                                showTransactionListDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }

                BalanceTabOptions.TRANSACTIONS -> {

                    if (balanceUiState.transactionListToShow.isEmpty()) {
                        item(span = { GridItemSpan(lazyGridSpan) }) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier
                            ) {
                                Text(text = stringResource(id = R.string.Balance_no_transactions))
                            }
                        }
                    }

                    items(
                        count = balanceUiState.transactionListToShow.size,
                        span = { GridItemSpan(lazyGridSpan) }
                    ) { position ->
                        val transactionCategoryGroup =
                            balanceUiState.transactionListToShow[position]
                        TransactionEntry(
                            transaction = transactionCategoryGroup.transaction,
                            category = transactionCategoryGroup.category,
                            currency = wallet.currency,
                            onClickTransaction = {
                                Screen.TransactionReport.navigate(
                                    transactionUUID = transactionCategoryGroup.transaction.id,
                                    navController = navController,
                                )
                            },
                            modifier = modifier
                        )
                    }
                }
            }

        }
    }

    TransactionsInCategoryAlertDialog(
        show = showTransactionListDialog,
        onDismissDialog = { showTransactionListDialog = it},
        transactionList = transactionListForDialog,
        currency = wallet.currency,
        navController = navController,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .padding(vertical = 16.dp)
    )
}