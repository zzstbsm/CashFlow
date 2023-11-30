package com.zhengzhou.cashflow.ui.walletOverview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.customUiElements.BottomNavigationBar
import com.zhengzhou.cashflow.customUiElements.CategoryIcon
import com.zhengzhou.cashflow.customUiElements.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.customUiElements.SectionTopAppBar
import com.zhengzhou.cashflow.data.Wallet
import java.util.UUID

@Composable
fun WalletOverviewScreen(
    walletUUID: UUID = UUID(0L,0L),
    currentScreen: com.zhengzhou.cashflow.navigation.NavigationCurrentScreen,
    setCurrentScreen: (com.zhengzhou.cashflow.navigation.NavigationCurrentScreen) -> Unit,
    navController: NavController
) {

    val walletOverviewViewModel: WalletOverviewViewModel = viewModel {
        WalletOverviewViewModel(
            walletUUID = walletUUID,
        )
    }
    val walletOverviewUiState by walletOverviewViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    com.zhengzhou.cashflow.navigation.ReloadPageAfterPopBackStack(
        pageRoute = com.zhengzhou.cashflow.navigation.Screen.WalletOverview.route,
        navController = navController
    ) {
        setCurrentScreen(com.zhengzhou.cashflow.navigation.NavigationCurrentScreen.WalletOverview)
        walletOverviewViewModel.reloadScreen()
    }

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
                        WalletOverviewAppBarAction(
                            walletOverviewUiState = walletOverviewUiState,
                            walletOverviewViewModel = walletOverviewViewModel,
                            navController = navController,
                        )
                    }
                )
            },
            content = { innerPadding ->
                WalletOverviewMainBody(
                    walletOverviewUiState = walletOverviewUiState,
                    walletOverviewViewModel = walletOverviewViewModel,
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
                WalletOverviewFloatingActionButton(
                    walletOverviewUiState = walletOverviewUiState,
                    walletOverviewViewModel = walletOverviewViewModel,
                    navController = navController,
                )
            }
        )
    }
}

@Composable
fun WalletOverviewMainBody(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
) {

    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        item {
            OverviewSection(
                walletOverviewUiState = walletOverviewUiState,
                walletOverviewViewModel = walletOverviewViewModel,
                navController = navController,
            )
        }
    }

    SelectWalletDialog(
        toShow = walletOverviewUiState.showSelectWallet,
        walletList = walletOverviewUiState.walletList,
        onDismissDialog = {
            walletOverviewViewModel.showSelectWalletDialog(false)
        },
        onSelectWallet = { wallet ->
            walletOverviewViewModel.selectWallet(wallet)
            walletOverviewViewModel.showSelectWalletDialog(false)
        },
    )

}

@Composable
private fun OverviewSection(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
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
            WalletInfoSection(
                walletOverviewUiState = walletOverviewUiState,
                walletOverviewViewModel = walletOverviewViewModel,
                modifier = modifier,
            )
            HorizontalDivider()
            TransactionListSection(
                walletOverviewUiState = walletOverviewUiState,
                walletOverviewViewModel = walletOverviewViewModel,
                navController = navController,
                modifier = modifier,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectWalletDialog(
    toShow: Boolean,
    walletList: List<Wallet>,
    onDismissDialog: () -> Unit,
    onSelectWallet: (Wallet) -> Unit,
) {

    if (toShow) {
        BasicAlertDialog(onDismissRequest = onDismissDialog) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.7f)
            ) {
                Text(
                    text = stringResource(id = R.string.WalletOverview_choose_wallet),
                    modifier = Modifier
                        .padding(8.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(walletList.size) { pos ->
                        val wallet = walletList[pos]

                        Card(
                            onClick = { onSelectWallet(wallet) },
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            ) {
                                CategoryIcon(
                                    iconName = wallet.iconName,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(16.dp)
                                )
                                Text(text = wallet.name)
                            }
                        }
                    }
                }
            }
        }
    }
}