package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.TransactionType
import java.util.UUID

@Composable
fun TransactionEditScreen(
    walletUUID: UUID,
    transactionType: TransactionType,
    transactionUUID: UUID,
    navController: NavController,
) {

    Scaffold(
        topBar = {
            TransactionEditTopAppBar(
                transactionType = transactionType,
                navController = navController,
            )
        },
        content = { paddingValues ->
            TransactionEditMainBody(
                modifier = Modifier.padding(paddingValues)
            )
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditTopAppBar(
    transactionType: TransactionType,
    navController: NavController,
) {
    TopAppBar(
        title = {
            Text(
                stringResource(id = transactionType.new_text)
            )
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
fun TransactionEditMainBody(
    modifier: Modifier = Modifier
) {

}