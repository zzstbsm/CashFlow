package com.zhengzhou.cashflow.transaction_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.tools.calculator.KeypadDigit
import com.zhengzhou.cashflow.tools.ui_elements.category.CategoryIcon
import com.zhengzhou.cashflow.tools.ui_elements.keypad.KeypadDigitButton

@Composable
internal fun AmountTextSection(
    amountString: String,
    wallet: Wallet,
    walletList: List<Wallet>,
    onWalletSelected: (Wallet) -> Unit,
    onBackKeyClick: () -> Unit,
    transactionType: TransactionType,
    modifier: Modifier = Modifier,
) {

    var showDropdownMenu by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = modifier
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .height(64.dp)
            .fillMaxWidth(),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            // Select the wallet
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    onClick = {
                        if (transactionType != TransactionType.Move)
                            showDropdownMenu = true
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CategoryIcon(
                            iconName = wallet.iconName,
                            contentDescription = null, // TODO: Add content description
                            modifier = modifier.size(24.dp)
                        )
                        Text(
                            text =  wallet.currency.name,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
                DropdownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false }
                ) {
                    walletList.forEach { walletInList ->
                        DropdownMenuItem(
                            leadingIcon = {
                                CategoryIcon(
                                    iconName = walletInList.iconName,
                                    contentDescription = walletInList.name
                                )
                            },
                            text = {
                                Text(text = walletInList.name)
                            },
                            onClick = {
                                onWalletSelected(walletInList)
                                showDropdownMenu = false
                            }
                        )
                    }
                }
            }

            // Amount of the transaction
            Text(
                text = amountString,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .weight(2f)
            )

            // Backspace for cleaning
            Box(
                modifier = modifier
                    .weight(1f),
            ) {
                IconButton(
                    modifier = modifier
                        .align(Alignment.BottomCenter),
                    onClick = {
                        onBackKeyClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_backspace),
                        contentDescription = "", // TODO: Add content description
                        modifier = modifier
                            .size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
internal fun KeypadTransactionSection(
    modifier: Modifier = Modifier,
    onDigitClick: (KeypadDigit) -> Unit
) {

    val keyList = listOf(
        KeypadDigit.Key1,
        KeypadDigit.Key2,
        KeypadDigit.Key3,
        KeypadDigit.KeyPlus,
        KeypadDigit.Key4,
        KeypadDigit.Key5,
        KeypadDigit.Key6,
        KeypadDigit.KeyMinus,
        KeypadDigit.Key7,
        KeypadDigit.Key8,
        KeypadDigit.Key9,
        KeypadDigit.KeyTimes,
        KeypadDigit.KeyDot,
        KeypadDigit.Key0,
        KeypadDigit.KeyEqual,
        KeypadDigit.KeyDivide,
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .padding(16.dp),
        userScrollEnabled = false,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(count = 16){pos ->
            KeypadDigitButton(
                key = keyList[pos],
                onClick = onDigitClick,
                modifier = modifier
                    .width(75.dp)
                    .height(90.dp)
                    .padding(4.dp),
                shape = CircleShape,
            )

        }
    }
}
