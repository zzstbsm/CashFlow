package com.zhengzhou.cashflow.ui.walletEdit

import android.text.format.DateFormat
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.tools.Calculator
import com.zhengzhou.cashflow.tools.KeypadDigit
import com.zhengzhou.cashflow.tools.mapCharToKeypadDigit
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
            navController = navController
        )
    }
    val walletEditUiState by walletEditViewModel.uiState.collectAsState()

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
            FloatingActionButton(onClick = { /*TODO*/ }) {
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

@OptIn(ExperimentalComposeUiApi::class)
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
        TextWalletCreationDate(
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun TextWalletCreationDate(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {
    var showDatePickerState by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = walletEditUiState.wallet.creationDate.time
    )

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        label = {
            Text(text = stringResource(id = R.string.WalletEdit_creation_date))
        },
        value = DateFormat.format(
            "EEEE, dd MMMM yyyy",
            walletEditUiState.wallet.creationDate
        ).toString(),
        onValueChange = { },
        modifier = modifier
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    showDatePickerState = true
                    focusManager.clearFocus()
                }
            }
        ,
        maxLines = 1,
    )

    if (showDatePickerState) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerState = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        walletEditViewModel.updateWalletCreationDate(datePickerState.selectedDateMillis)
                        showDatePickerState = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm)
                    )
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun TextWalletSetBudget(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {

}