package com.zhengzhou.cashflow.all_transactions

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.all_transactions.view_model.AllTransactionsUiState
import com.zhengzhou.cashflow.all_transactions.view_model.AllTransactionsViewModel
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.tools.ui_elements.transaction.SectionTransactionEntry
import kotlinx.coroutines.launch

@Composable
internal fun AllTransactionsMainBody(
    allTransactionsUiState: AllTransactionsUiState,
    allTransactionsViewModel: AllTransactionsViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
) {

    val lazyListStateTransactionList = rememberLazyListState(
        initialFirstVisibleItemIndex = 0,
        initialFirstVisibleItemScrollOffset = 0
    )

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        MonthsTabRow(
            currentTab = allTransactionsUiState.shownTab,
            dateTabWithIndexList = allTransactionsUiState.dateTabWithIndexList,
            setCurrentTab = { currentTabIndex ->
                allTransactionsViewModel.updateShownTab(currentTabIndex)
            },
            onClickTab = { indexInTab, indexInList ->
                coroutineScope.launch {
                    lazyListStateTransactionList.animateScrollToItem(
                        index = indexInList,
                    )
                }
                allTransactionsViewModel.updateShownTab(indexInTab)
            },
            tabOfFirstVisibleItem = remember {
                derivedStateOf { lazyListStateTransactionList.firstVisibleItemIndex }
            }
        )
        LazyColumn(
            state = lazyListStateTransactionList,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            val transactionAndCategoryList = allTransactionsUiState.transactionListToShow
            val dateTabWithIndexList = allTransactionsUiState.dateTabWithIndexList
            items(transactionAndCategoryList.size) { position ->


                val transaction = transactionAndCategoryList[position].transaction
                val category = transactionAndCategoryList[position].category

                if (position in dateTabWithIndexList.map { it.indexOfTransaction }) {
                    Text(
                        text = DateFormat.format("MMMM yyyy",transaction.date).toString(),
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .align(Alignment.Start)
                    )
                }

                SectionTransactionEntry(
                    transaction = transaction,
                    category = category,
                    currency = allTransactionsUiState.currency,
                    onClickTransaction = {
                        Screen.TransactionReport.navigate(
                            transactionUUID = transaction.id,
                            navController = navController
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun MonthsTabRow(
    currentTab: Int,
    dateTabWithIndexList: List<DateTabWithIndex>,
    setCurrentTab: (Int) -> Unit,
    onClickTab: (Int, Int) -> Unit,
    tabOfFirstVisibleItem: State<Int>,
) {

    if (currentTab >= 0) {

        val scope = rememberCoroutineScope()
        val lazyListStateTabRow = rememberLazyListState(
            currentTab
        )

        LaunchedEffect(key1 = tabOfFirstVisibleItem.value) {
            scope.launch {
                val tabThatShouldBeSelected = dateTabWithIndexList
                    .filter { it.indexOfTransaction >= tabOfFirstVisibleItem.value }
                    .maxBy { it.indexOfTab }
                lazyListStateTabRow.animateScrollToItem(tabThatShouldBeSelected.indexOfTab)
                setCurrentTab(tabThatShouldBeSelected.indexOfTab)
            }
        }

        LazyRow(
            state = lazyListStateTabRow,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                count = dateTabWithIndexList.size
            ) { index ->
                val dateTabWithIndex = dateTabWithIndexList[index]
                OutlinedButton(
                    onClick = {
                        onClickTab(index,dateTabWithIndex.indexOfTransaction)
                    },
                    enabled = currentTab != index,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = DateFormat.format(
                            "MMMM yyyy",
                            dateTabWithIndex.date
                        ).toString()
                    )
                }
            }
        }
        HorizontalDivider()
    }
}