package com.zhengzhou.cashflow.ui.walletOverview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.zhengzhou.cashflow.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.Screen
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar
import com.zhengzhou.cashflow.ui.SectionTransactionEntry
import java.util.UUID

@Composable
fun WalletOverviewScreen(
    walletUUID: UUID = UUID(0L,0L),
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController
) {

    val walletOverviewViewModel: WalletOverviewViewModel = viewModel {
        WalletOverviewViewModel(
            walletUUID = walletUUID,
        )
    }
    val walletOverviewUiState by walletOverviewViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.WalletOverview.route,
        navController = navController
    ) {
        setCurrentScreen(NavigationCurrentScreen.WalletOverview)
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
fun WalletOverviewAppBarAction(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    navController: NavController,
) {

    var openMenu by remember { mutableStateOf(false)}

    Row {
        if (!walletOverviewUiState.ifZeroWallet) {
            IconButton(
                onClick = { 
                    Screen.WalletEdit.navigate(
                        walletID = walletOverviewUiState.wallet.id,
                        navController = navController,
                    ) 
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = stringResource(id = R.string.WalletOverview_edit_wallet)
                )
            }
            
            IconButton(onClick = { openMenu = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription =  stringResource(R.string.accessibility_menu_action_open),
                )
            }
        }
    }
    DropdownMenu(
        expanded = openMenu,
        onDismissRequest = {
            openMenu = false
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.nav_name_wallet_add))
            },
            onClick = {
                Screen.WalletEdit.navigate(
                    walletID  = UUID(0L,0L),
                    navController = navController,
                )
            },
        )

        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.nav_name_wallet_delete))
            },
            onClick = {
                walletOverviewViewModel.deleteShownWallet()
                openMenu = false

            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletOverviewMainBody(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
) {

    val walletList by walletOverviewViewModel.walletList.collectAsState()

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
        item {
            BudgetSection(
                walletOverviewUiState = walletOverviewUiState,
                walletOverviewViewModel = walletOverviewViewModel,
            )
        }
    }

    SelectWalletDialog(
        toShow = walletOverviewUiState.showSelectWallet,
        walletList = walletList,
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
private fun CustomCard(
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
    navController: NavController,
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
            navController = navController,
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
                stringResource(id = R.string.WalletOverview_no_wallet)
            } else {
                walletOverviewUiState.wallet.name
            },
            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.WalletOverview_balance)
            )
            Text(
                // TODO fix
                text = walletOverviewViewModel.formatCurrency(walletOverviewUiState.currentAmountInTheWallet)
            )
        }
    }
}

@Composable
private fun TransactionListSection(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = stringResource(
                id = if (walletOverviewUiState.transactionAndCategoryList.isEmpty()) {
                    R.string.WalletOverview_no_transactions
                } else {
                    R.string.WalletOverview_recent_transactions
                }
            ),
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
        )

        if (walletOverviewUiState.transactionAndCategoryList.isNotEmpty()) {
            walletOverviewUiState.transactionAndCategoryList.forEach { item ->
                val transaction = item.transaction
                val category = item.category
                SectionTransactionEntry(
                    transaction = transaction,
                    category = category,
                    currencyFormatter = walletOverviewViewModel.currencyFormatter,
                    onClickTransaction = {
                        Screen.TransactionReport.navigate(
                            transactionUUID = transaction.id,
                            navController = navController,
                        )
                    },
                    modifier = modifier,
                )
            }
        }
    }
}

@Composable
private fun BudgetSection(
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
@Composable
private fun WalletOverviewFloatingActionButton(
    walletOverviewUiState: WalletOverviewUiState,
    walletOverviewViewModel: WalletOverviewViewModel,
    navController: NavController,
) {
    
    val spacerSpaceWidthModifier: Modifier = Modifier.width(8.dp)

    val textId: Int
    val iconId: Int
    val onClick: () -> Unit

    if (walletOverviewUiState.ifZeroWallet) {
        textId = R.string.WalletOverview_add_wallet
        iconId = R.drawable.ic_add
        onClick = {
            Screen.WalletEdit.navigate(
                walletID = UUID(0L,0L),
                navController = navController,
            )
        }
    } else {
        textId = R.string.WalletOverview_select_wallet
        iconId = R.drawable.ic_wallet
        onClick = {
            walletOverviewViewModel.updateWalletList()
            walletOverviewViewModel.showSelectWalletDialog(true)
        }
    }

    ExtendedFloatingActionButton(
        onClick = {
            onClick()
        }
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = stringResource(id = textId),
        )
        Spacer(modifier = spacerSpaceWidthModifier)
        Text(
            text = stringResource(id = textId)
        )
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
        AlertDialog(onDismissRequest = onDismissDialog) {
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
                                Icon(
                                    painter = painterResource(id = wallet.iconId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                                Spacer(modifier = Modifier
                                    .width(16.dp))
                                Text(text = wallet.name)
                            }
                        }
                    }
                }
            }
        }
    }
}