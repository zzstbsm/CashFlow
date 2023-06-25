package com.zhengzhou.cashflow.ui.walletOverview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.BackHandler
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.Screen
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar
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
            navController = navController,
        )
    }
    val walletOverviewUiState by walletOverviewViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.WalletOverview.route,
        navController = navController
    ) {
        setCurrentScreen(NavigationCurrentScreen.WalletOverview)
        walletOverviewViewModel.reloadScreen(walletUUID)
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
                            Screen.WalletEdit.navigate(
                                walletID = UUID(0L,0L),
                                navController = navController,
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = stringResource(id = R.string.WalletOverview_add_wallet),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.nav_name_wallet_add)
                        )
                    }
                }
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
                text = walletOverviewViewModel.formatCurrency(walletOverviewUiState.wallet.startAmount)
            )
        }
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
                    R.string.WalletOverview_no_transactions
                } else {
                    R.string.WalletOverview_recent_transactions
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