package com.zhengzhou.cashflow.wallet_overview

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
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.database.api.repository.RepositoryInterface
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.navigation.functions.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.tools.ui_elements.category.CategoryIcon
import com.zhengzhou.cashflow.tools.ui_elements.navigation.BottomNavigationBar
import com.zhengzhou.cashflow.tools.ui_elements.navigation.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.tools.ui_elements.navigation.SectionTopAppBar
import com.zhengzhou.cashflow.wallet_overview.view_model.WalletOverviewUiState
import com.zhengzhou.cashflow.wallet_overview.view_model.WalletOverviewViewModel
import java.util.UUID

@Composable
fun WalletOverviewScreen(
    repository: RepositoryInterface,
    walletUUID: UUID = UUID(0L,0L),
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    navController: NavController
) {

    val walletOverviewViewModel: WalletOverviewViewModel = viewModel {
        WalletOverviewViewModel(
            repository = repository,
            walletUUID = walletUUID,
        )
    }
    val walletOverviewUiState by walletOverviewViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.WalletOverview.route,
        navController = navController
    ) {
        setCurrentScreen(ApplicationScreensEnum.WalletOverview)
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
internal fun WalletOverviewMainBody(
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
internal fun OverviewSection(
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
                navController = navController,
                modifier = modifier,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectWalletDialog(
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
                    text = stringResource(id = R.string.choose_wallet),
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text(text = wallet.name)
                                    Text(text = wallet.currency.iconEmojiUnicode)
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}