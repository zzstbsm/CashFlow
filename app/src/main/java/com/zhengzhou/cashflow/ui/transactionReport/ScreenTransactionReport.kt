package com.zhengzhou.cashflow.ui.transactionReport

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.Screen
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.tools.mapIconsFromName
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TransactionReportScreen(
    transactionUUID: UUID,
    navController: NavController,
) {

    val transactionReportViewModel: TransactionReportViewModel = viewModel {
        TransactionReportViewModel(transactionUUID = transactionUUID)
    }
    val transactionReportUiState by transactionReportViewModel.uiState.collectAsState()

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.TransactionReport.route,
        navController = navController
    ) {
        transactionReportViewModel.loadTransactionReport(transactionUUID)
    }

    Scaffold(
        topBar = {
             TransactionReportTopAppBar(
                 transactionType = transactionReportUiState.transactionType,
                 onNavigationIconClick = {
                    navController.popBackStack()
                 },
                 onEditClick = {
                     Screen.TransactionEdit.navigate(
                         transactionType = transactionReportUiState.transactionType,
                         transactionUUID = transactionUUID,
                         isBlueprint = false,
                         navController = navController,
                     )
                 },
             )
        },
        content = { paddingValues ->
            TransactionReportMainBody(
                transactionReportUiState = transactionReportUiState,
                transactionReportViewModel = transactionReportViewModel,
                contentPadding = paddingValues,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionReportTopAppBar(
    transactionType: TransactionType,
    onNavigationIconClick: (Boolean) -> Unit,
    onEditClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                stringResource(id = transactionType.text)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onNavigationIconClick(true) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = stringResource(id = R.string.nav_back)
                )
            }
        },
        actions = {
            IconButton(onClick = { onEditClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = stringResource(id = R.string.TransactionReport_edit_transaction) 
                )
            }
        }
    )
}

@Composable
private fun TransactionReportMainBody(
    transactionReportUiState: TransactionReportUiState,
    transactionReportViewModel: TransactionReportViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(contentPadding)
    ) {
        item {
            Card(
                //horizontalAlignment = Alignment.Start,
                //verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                            horizontal = 16.dp,
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = Currency.formatCurrency(
                                currency = Currency.setCurrencyFormatter(transactionReportUiState.wallet.currency.abbreviation),
                                amount = transactionReportUiState.transaction.amount,
                            ),
                            fontStyle = FontStyle.Normal,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .weight(5f)
                        )
                        if (transactionReportUiState.category.iconName == "loading") {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .weight(1f)
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = mapIconsFromName[transactionReportUiState.category.iconName]!!),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                    }
                    Text(
                        text = transactionReportUiState.transaction.description,
                    )
                    Text(
                        text = DateFormat.format(
                            "EEEE, dd MMMM yyyy",
                            transactionReportUiState.transaction.date,
                        ).toString()
                    )
                }
            }
        }
    }
}