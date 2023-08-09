package com.zhengzhou.cashflow.ui.commonTransactions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.ui.CategoryIcon
import com.zhengzhou.cashflow.ui.DropdownTextFieldMenu
import com.zhengzhou.cashflow.ui.TagListLazyStaggeredHorizontalGrid
import java.text.NumberFormat


@Composable
fun SingleTransactionVisibleSection(
    transaction: Transaction,
    category: Category,
    currencyFormatter: NumberFormat,
    onCreateTransaction: () -> Unit,
    onEditTransaction: () -> Unit,
    ifTransactionOpen: Boolean,
    onOpenTransaction: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        SingleTransactionDescriptionPart(
            transaction = transaction,
            category = category,
            currencyFormatter = currencyFormatter,
            onDescriptionSectionClick = { onOpenTransaction(!ifTransactionOpen) },
            modifier = Modifier.weight(3f),
        )
        SingleTransactionActionButtons(
            ifTransactionOpen = ifTransactionOpen,
            onOpenTransaction = onOpenTransaction,
            onCreateTransaction = onCreateTransaction,
            onEditTransaction = onEditTransaction,
            modifier = Modifier.weight(2f)
        )

    }
}

@Composable
private fun SingleTransactionDescriptionPart(
    transaction: Transaction,
    category: Category,
    currencyFormatter: NumberFormat,
    onDescriptionSectionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clickable {
                onDescriptionSectionClick()
            }
    ) {
        val transactionTypeText = stringResource(transaction.transactionType.text)
        Text(
            text = transactionTypeText + ": " + transaction.description,
            softWrap = true,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            CategoryIcon(
                iconName = category.iconName,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = Currency.formatCurrency(currencyFormatter, transaction.amount))
        }
    }
}

@Composable
private fun SingleTransactionActionButtons(
    ifTransactionOpen: Boolean,
    onOpenTransaction: (Boolean) -> Unit,
    onCreateTransaction: () -> Unit,
    onEditTransaction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier
    ) {
        IconButton(onClick = { onCreateTransaction() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null,
            )
        }
        IconButton(onClick = { onEditTransaction() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = null,
            )
        }
        IconButton(onClick = { onOpenTransaction(!ifTransactionOpen) }) {
            Icon(
                painter = painterResource(
                    id = if (ifTransactionOpen) R.drawable.ic_up else R.drawable.ic_down
                ),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun SingleTransactionHiddenSection(
    wallet: Wallet,
    walletList: List<Wallet>,
    tagList: List<Tag>,
    onChangeWallet: (Wallet) -> Unit,
    onEditTransactionModel: () -> Unit,
    onDeleteTransaction: () -> Unit,
) {
    var ifWalletChoiceOpen by remember {
        mutableStateOf(false)
    }

    HorizontalDivider()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        CategoryIcon(
            iconName = wallet.iconName,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        DropdownTextFieldMenu(
            label = stringResource(id = R.string.wallet),
            value = wallet.name,
            expanded = ifWalletChoiceOpen,
            onChangeExpanded = { ifShow ->
                ifWalletChoiceOpen = ifShow
            },
            dropdownMenuContent = {
                walletList.forEach { walletInList ->
                    DropdownMenuItem(
                        leadingIcon = {
                            CategoryIcon(
                                iconName = walletInList.iconName,
                                contentDescription = walletInList.name
                            )
                        },
                        text = { Text(walletInList.name) },
                        onClick = {
                            onChangeWallet(walletInList)
                            ifWalletChoiceOpen = false
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }

    HorizontalDivider()

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text(text = stringResource(id = R.string.CommonTransactions_tag))

        TagListLazyStaggeredHorizontalGrid(
            tagList = tagList,
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )
    }

    HorizontalDivider()

    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(onClick = onDeleteTransaction) {
            Text(text = stringResource(id = R.string.delete))
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(onClick = onEditTransactionModel) {
            Text(text = stringResource(id = R.string.CommonTransactions_edit_model))
        }
    }

}