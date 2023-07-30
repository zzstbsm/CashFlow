package com.zhengzhou.cashflow.ui.walletEdit

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.Screen
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.tools.mapIconsFromName
import com.zhengzhou.cashflow.ui.DateSelector
import com.zhengzhou.cashflow.ui.DropdownTextFieldMenu
import com.zhengzhou.cashflow.ui.MoneyTextField
import java.util.UUID

private enum class WalletEditOption(
    @StringRes
    val title: Int,
){
    ADD(
        title = R.string.nav_name_wallet_add
    ),
    EDIT(
        title = R.string.nav_name_wallet_edit
    );

    companion object {
        fun chooseOption(id: UUID) : WalletEditOption{
            return if(id == UUID(0L,0L)) {
                ADD
            } else {
                EDIT
            }
        }
    }

}

@Composable
fun WalletEditScreen(
    walletUUID: UUID,
    navController: NavController
) {

    val walletEditOption = WalletEditOption.chooseOption(walletUUID)

    val walletEditViewModel: WalletEditViewModel = viewModel {
        WalletEditViewModel(
            walletUUID = walletUUID,
        )
    }
    val walletEditUiState by walletEditViewModel.uiState.collectAsState()

    var keepLoadingScreen by remember {
        mutableStateOf(true)
    }

    if (walletEditUiState.isLoading && keepLoadingScreen) {
        Dialog(onDismissRequest = {
            keepLoadingScreen = false
            navController.popBackStack()
        }) {
            CircularProgressIndicator()
        }
    }

    Scaffold(
        topBar = {
            WalletEditTopAppBar(
                walletEditOption = walletEditOption,
                navController = navController,
            )
        },
        content = { innerPadding ->
            WalletEditMainBody(
                walletEditUiState = walletEditUiState,
                walletEditViewModel = walletEditViewModel,
                innerPadding = innerPadding,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    if (walletEditUiState.isErrorWalletNameInUse) {
                        EventMessages.sendMessageId(R.string.WalletEdit_error_wallet_name_already_in_use)
                    } else if (walletEditUiState.isErrorWalletNameNotValid) {
                        EventMessages.sendMessageId(R.string.WalletEdit_error_wallet_name_not_valid)
                    } else {
                        walletEditViewModel.saveWallet()
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(
                                Screen.WalletOverview.keyWalletUUID(),
                                walletEditUiState.wallet.id.toString()
                            )
                        navController.popBackStack(
                            route = Screen.WalletOverview.route,
                            inclusive = false
                        )
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = null,
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WalletEditTopAppBar(
    walletEditOption: WalletEditOption,
    navController: NavController,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = walletEditOption.title))
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = stringResource(id = R.string.nav_back)
                )
            }
        }
    )
}

@Composable
fun WalletEditMainBody(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    innerPadding: PaddingValues,
) {

    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)

    Column(
        modifier = Modifier.padding(innerPadding),
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = modifier,
        ) {
            TextWalletIcon(
                walletEditUiState = walletEditUiState,
                walletEditViewModel = walletEditViewModel,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(1f)
            )
            TextWalletName(
                walletEditUiState = walletEditUiState,
                walletEditViewModel = walletEditViewModel,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .weight(3f),
            )
        }
        MoneyTextField(
            label = stringResource(id = R.string.WalletEdit_initial_amount),
            amountOnScreen = walletEditViewModel.getOnScreenString(),
            onKeyPressed = { pressedKey ->
                walletEditViewModel.onKeyPressed(pressedKey)
            },
            modifier = modifier,
        )
        Row {
            DateSelector(
                label = stringResource(id = R.string.WalletEdit_creation_date),
                dateFormat = "EEEE, dd MMMM yyyy",
                date = walletEditUiState.wallet.creationDate,
                onSelectDate = { millis ->
                    walletEditViewModel.updateWalletCreationDate(millis)
                },
                modifier = modifier.weight(2f),
            )
            TextWalletCurrencyChooser(
                walletEditUiState = walletEditUiState,
                walletEditViewModel = walletEditViewModel,
                modifier = modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        SectionWalletBudget(
            walletEditUiState = walletEditUiState,
            walletEditViewModel = walletEditViewModel,
            modifier = modifier
        )

    }

}

@Composable
private fun TextWalletName(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        label = {
            Text(text = stringResource(id = R.string.WalletEdit_name))
        },
        value = walletEditUiState.wallet.name,
        onValueChange = {
            if (!(it.isNotEmpty() && it.last() == '\n')) {
                walletEditViewModel.updateWalletName(it)
            }
        },
        modifier = modifier,
        maxLines = 1,
        isError = (
            walletEditUiState.isErrorWalletNameInUse ||
            walletEditUiState.isErrorWalletNameNotValid
        ),
        supportingText = {
            if (walletEditUiState.isErrorWalletNameNotValid) {
                Text(stringResource(id = R.string.WalletEdit_error_wallet_name_not_valid))
            } else if (walletEditUiState.isErrorWalletNameInUse) {
                Text(stringResource(id = R.string.WalletEdit_error_wallet_name_already_in_use))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextWalletIcon(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

    val currentIcon = walletEditUiState.wallet.iconName
    var showDialog by remember {
        mutableStateOf(false)
    }

    OutlinedButton(
        onClick = { showDialog = true },
        shape = RoundedCornerShape(4.dp),
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = mapIconsFromName[currentIcon]!!),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.ManageCategories_choose_category_icon),
                    modifier = Modifier.padding(8.dp)
                )
                LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                    mapIconsFromName.forEach { (iconName, resourceId) ->
                        item {
                            OutlinedButton(
                                enabled = iconName != currentIcon,
                                onClick = {
                                    walletEditViewModel.updateWalletIcon(iconName = iconName)
                                    showDialog = false
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier= Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                            ) {
                                Icon(
                                    painter = painterResource(id = resourceId),
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TextWalletCurrencyChooser(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

    var showDropDownMenu by remember { mutableStateOf(false) }

    DropdownTextFieldMenu(
        label = stringResource(id = R.string.currency),
        value = walletEditUiState.wallet.currency.abbreviation,
        expanded = showDropDownMenu,
        onChangeExpanded = { ifShow ->
            showDropDownMenu = ifShow
        },
        dropdownMenuContent = {
            Currency.supportedCurrencyList().forEach { currency ->

                DropdownMenuItem(
                    leadingIcon = {
                        Text(
                            text = currency.iconEmojiUnicode,
                            style = TextStyle(fontSize = 20.sp)
                        )
                    },
                    text = {
                        Text(
                            text = currency.abbreviation
                        )
                    },
                    onClick = {
                        walletEditViewModel.updateWalletCurrency(currency = currency)
                        showDropDownMenu = false
                    }
                )
            }
        },
        modifier = modifier,
    )
}
