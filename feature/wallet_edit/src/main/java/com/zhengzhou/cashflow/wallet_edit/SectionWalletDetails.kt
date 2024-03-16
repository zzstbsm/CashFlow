package com.zhengzhou.cashflow.wallet_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.themes.icons.IconsMappedForDB
import com.zhengzhou.cashflow.tools.ui_elements.category.CategoryIcon
import com.zhengzhou.cashflow.tools.ui_elements.date_selector.DateSelector
import com.zhengzhou.cashflow.tools.ui_elements.dropdown_menu.DropdownTextFieldMenu
import com.zhengzhou.cashflow.tools.ui_elements.icon_choice.IconChoiceDialog
import com.zhengzhou.cashflow.wallet_edit.view_model.WalletEditUiState
import com.zhengzhou.cashflow.wallet_edit.view_model.WalletEditViewModel
import java.util.Date

@Composable
internal fun WalletDetailsSection(
    walletEditUiState: WalletEditUiState,
    walletEditViewModel: WalletEditViewModel,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
        modifier = modifier,
    ) {
        TextWalletIcon(
            walletEditUiState = walletEditUiState,
            walletEditViewModel = walletEditViewModel,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
                .padding(top = 8.dp)
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
        label = stringResource(id = R.string.initial_amount),
        amountOnScreen = walletEditUiState.amountOnScreen,
        onValueChange = { newText ->
            if (newText.count { it == '.' } < 2)
                walletEditViewModel.updateAmountOnScreen(newText)
        },
        isError = walletEditUiState.isErrorAmountOnScreen,
        modifier = modifier,
    )
    Row {
        DateSelector(
            label = stringResource(id = R.string.creation_date),
            dateFormat = "EEEE, dd MMMM yyyy",
            date = walletEditUiState.wallet.creationDate,
            onSelectDate = { millis ->
                walletEditViewModel.updateWallet(
                    creationDate = Date(millis ?: walletEditUiState.wallet.creationDate.time)
                )
            },
            modifier = modifier.weight(2f),
        )
        TextWalletCurrencyChooser(
            walletEditUiState = walletEditUiState,
            walletEditViewModel = walletEditViewModel,
            modifier = modifier.weight(1f),
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
            Text(text = stringResource(id = R.string.name))
        },
        value = walletEditUiState.wallet.name,
        onValueChange = {
            if (!(it.isNotEmpty() && it.last() == '\n')) {
                walletEditViewModel.updateWallet(name = it)
            }
        },
        modifier = modifier.testTag(
            WalletEditTestTag.TAG_TEST_FIELD_WALLET_NAME
        ),
        maxLines = 1,
        isError = (
                walletEditUiState.isErrorWalletNameInUse ||
                        walletEditUiState.isErrorWalletNameNotValid
                ),
        supportingText = {
            Text(
                text =
                if (walletEditUiState.isErrorWalletNameNotValid)
                    stringResource(id = R.string.error_wallet_name_not_valid)
                else if (walletEditUiState.isErrorWalletNameInUse)
                    stringResource(id = R.string.error_wallet_name_already_in_use)
                else "",
                modifier = Modifier.testTag(
                    tag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_NAME_SUPPORTING_TEXT
                )
            )
        }
    )
}

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
        modifier = modifier.testTag(
            WalletEditTestTag.TAG_ICON_FIELD_ICON
        ),
    ) {
        CategoryIcon(
            iconName = currentIcon,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }

    if (showDialog) {
        IconChoiceDialog(
            text = stringResource(id = R.string.choose_wallet_icon),
            iconList = IconsMappedForDB.values()
                .toList()
                .filter { it.wallet },
            onDismissRequest = { showDialog = false },
            currentSelectedIcon = currentIcon,
            onChooseIcon = { chosenIcon ->
                walletEditViewModel.updateWallet(
                    iconName = chosenIcon
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MoneyTextField(
    label: String,
    amountOnScreen: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {

    OutlinedTextField(
        label = {
            Text(text = label)
        },
        value = amountOnScreen,
        onValueChange = onValueChange,
        modifier = modifier.testTag(
            WalletEditTestTag.TAG_TEST_FIELD_WALLET_AMOUNT
        ),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        isError = isError,
        supportingText = {
            Text(
                text =
                if (isError)
                    stringResource(id = R.string.error_amount_non_valid)
                else "",
                modifier = Modifier.testTag(
                    tag = WalletEditTestTag.TAG_TEST_FIELD_WALLET_AMOUNT_SUPPORTING_TEXT
                )
            )
        }
    )
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
        value = walletEditUiState.wallet.currency.name,
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
                            text = currency.name
                        )
                    },
                    onClick = {
                        walletEditViewModel.updateWallet(currency = currency)
                        showDropDownMenu = false
                    }
                )
            }
        },
        modifier = modifier,
    )
}
