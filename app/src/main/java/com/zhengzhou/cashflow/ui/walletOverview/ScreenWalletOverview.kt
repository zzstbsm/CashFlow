package com.zhengzhou.cashflow.ui.walletOverview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar

@Composable
fun WalletOverviewScreen(
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController
) {

    val walletOverviewViewModel: WalletOverviewViewModel = viewModel {
        WalletOverviewViewModel(
            navController = navController
        )
    }
    val walletOverviewUiState by walletOverviewViewModel.uiState.collectAsState()
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
                )
            },
            content = { innerPadding ->
                WalletOverviewMainBody(
                    walletOverviewUiState = walletOverviewUiState,
                    walletOverviewViewModel = walletOverviewViewModel,
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
                if (walletOverviewUiState.ifZeroWallet) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            NavigationCurrentScreen.WalletEdit.navigate(
                                navController = navController
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = stringResource(id = R.string.add_wallet), 
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.add_wallet) 
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun WalletOverviewMainBody(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    innerPadding: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        item {
            OverviewSection(
                walletOverviewUiState = walletOverviewUiState,
                walletOverviewViewModel = walletOverviewViewModel,
            )
        }
        item {
            BudgetSection(
                walletOverviewUiState = walletOverviewUiState,
                walletOverviewViewModel = walletOverviewViewModel,
            )
        }
    }
}

@Composable
private fun CustomCard(
    modifier: Modifier = Modifier,
    content: @Composable() (ColumnScope.() -> Unit),
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun OverviewSection(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    modifier: Modifier = Modifier,
) {
    CustomCard {
        WalletInfoSection(
            walletOverviewUiState = walletOverviewUiState,
            walletOverviewViewModel = walletOverviewViewModel,
            modifier = modifier,
        )
        Divider()
        TransactionListSection(
            walletOverviewUiState = walletOverviewUiState,
            walletOverviewViewModel = walletOverviewViewModel,
            modifier = modifier,
        )
    }
}

@Composable
private fun WalletInfoSection(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = if (walletOverviewUiState.ifZeroWallet) {
                stringResource(id = R.string.no_wallet)
            } else {
                "Euro" // TODO: put amount
            },
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun TransactionListSection(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = stringResource(
                id = if (walletOverviewUiState.ifZeroWallet) {
                    R.string.no_transactions
                } else {
                    R.string.recent_transactions
                }
            ),
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable fun BudgetSection(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    modifier: Modifier = Modifier,
) {
    CustomCard {
        Text(
            text = "Budget section",
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
        )
    }
}