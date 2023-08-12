package com.zhengzhou.cashflow.ui.balance

import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun BalanceTabOptionsSelector(
    onSelectTab: (BalanceTabOptions) -> Unit
) {

    var activeTab by remember {
        mutableIntStateOf(0)
    }

    TabRow(selectedTabIndex = activeTab) {
        BalanceTabOptions.values().toList().forEachIndexed { index, balanceTabOptions ->
            Tab(
               selected = activeTab == index,
               onClick = {
                   activeTab = index
                   onSelectTab(balanceTabOptions)
               },
               text = { Text(text = stringResource(id = balanceTabOptions.text)) },
               icon = {
                   Icon(
                       painter = painterResource(id = balanceTabOptions.icon),
                       contentDescription = stringResource(id = balanceTabOptions.text)
                   )
               }
            )
        }
    }
}