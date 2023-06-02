package com.zhengzhou.cashflow.ui.balance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zhengzhou.cashflow.R

@Preview
@Composable
private fun BalanceScreenPreview(){
    BalanceScreen(
        navController = rememberNavController()
    )
}

@Composable
fun BalanceScreen(
    navController: NavController
) {

    val balanceViewModel: BalanceViewModel = viewModel()
    val uiState by balanceViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
             BalanceTopAppBar()
        },
        content = {innerPadding ->
            BalanceMainBody(
                contentPadding = innerPadding
            )
        },
        bottomBar = { },
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BalanceTopAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.wallet))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    // TODO navigation
                },
                content = {
                    Image(
                        painter  = painterResource(id = R.drawable.ic_menu),
                        contentDescription = "", // TODO add accessibility menu
                    )
                },
                modifier = modifier
            )
        }
    )
}

@Composable
private fun BalanceMainBody(
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(contentPadding)
    ) {

    }
}