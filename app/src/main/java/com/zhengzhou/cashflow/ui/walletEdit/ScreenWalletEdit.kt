package com.zhengzhou.cashflow.ui.walletEdit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.BackHandler
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.tools.Calculator
import com.zhengzhou.cashflow.tools.KeypadDigit
import com.zhengzhou.cashflow.tools.mapCharToKeypadDigit
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.ui.SectionTopAppBar

@Composable
fun WalletEditScreen(
    walletEditOption: WalletEditOption,
    navController: NavController
) {

    val walletEditViewModel: WalletEditViewModel = viewModel {
        WalletEditViewModel(
            navController = navController
        )
    }
    val walletEditUiState by walletEditViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            WalletEditTopAppBar(navController = navController)
        },
        content = { innerPadding ->
            WalletEditMainBody(
                walletEditUiState = walletEditUiState,
                walletEditViewModel = walletEditViewModel,
                innerPadding = innerPadding,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WalletEditTopAppBar(
    navController: NavController,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.add_wallet))
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WalletEditMainBody(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    innerPadding: PaddingValues,
) {

    val modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)


    Column(
        modifier = Modifier.padding(innerPadding)
    ) {
        TextWalletName(
            walletEditUiState = walletEditUiState,
            walletEditViewModel = walletEditViewModel,
            modifier = modifier,
        )
        TextWalletAmount(
            walletEditUiState = walletEditUiState,
            walletEditViewModel = walletEditViewModel,
            modifier = modifier,
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
            walletEditViewModel.updateWalletName(it)
        },
        modifier = modifier,
        maxLines = 1,
    )
}

@Composable
private fun TextWalletAmount(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

    val calculator: Calculator by remember { mutableStateOf(Calculator()) }
    var amountOnScreen by remember { mutableStateOf(calculator.onScreenString()) }

    OutlinedTextField(
        label = {
            Text(text = stringResource(id = R.string.WalletEdit_initial_amount))
        },
        value = amountOnScreen,
        onValueChange = { newText ->
            if (newText.length >= calculator.onScreenString().length) {
                val newDigit = newText.last()
                val newKey: KeypadDigit? = mapCharToKeypadDigit(newDigit)
                if (newKey != null) {
                    calculator.addKey(newKey)
                }
            } else {
                calculator.dropLastDigit()
            }
            amountOnScreen = calculator.onScreenString()
            walletEditViewModel.updateWalletAmount(amount = amountOnScreen.toFloat())
        },
        modifier = modifier,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}


@Composable
private fun TextWalletIcon(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

}@Composable
private fun TextWalletBudgetFlag(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

}

@Composable
private fun TextWalletCreationDate(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

}

@Composable
private fun TextWalletSetBudget(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

}