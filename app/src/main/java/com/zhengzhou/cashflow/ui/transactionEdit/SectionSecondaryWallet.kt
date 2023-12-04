package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Wallet
import com.zhengzhou.cashflow.themes.ui_elements.category.CategoryIcon

@Composable
fun SecondaryWalletSection(
    primaryWallet: Wallet,
    onSelectPrimaryWallet: (Wallet) -> Unit,
    secondaryWallet: Wallet,
    onSelectSecondaryWallet: (Wallet) -> Unit,
    walletList: List<Wallet>,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SelectWalletCard(
            wallet = primaryWallet,
            walletList = walletList,
            onChooseWallet = onSelectPrimaryWallet,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_double_down),
            contentDescription = null
        )
        SelectWalletCard(
            wallet = secondaryWallet,
            walletList = walletList,
            onChooseWallet = onSelectSecondaryWallet,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectWalletCard(
    wallet: Wallet,
    walletList: List<Wallet>,
    onChooseWallet: (Wallet) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDropdown by remember {
        mutableStateOf(false)
    }
    OutlinedCard(
        onClick = {
            showDropdown = true
        },
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            CategoryIcon(
                iconName = wallet.iconName,
                contentDescription = wallet.name
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(wallet.name)
        }
        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false }
        ) {
            walletList.forEach { inWalletList ->
                DropdownMenuItem(
                    text = {
                        Text(text = inWalletList.name)
                    },
                    onClick = {
                        showDropdown = false
                        onChooseWallet(inWalletList)
                    },
                    leadingIcon = {
                        CategoryIcon(
                            iconName = inWalletList.iconName,
                            contentDescription = inWalletList.name
                        )
                    }
                )
            }
        }
    }

}