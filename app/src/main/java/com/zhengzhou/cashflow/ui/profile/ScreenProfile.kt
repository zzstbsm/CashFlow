package com.zhengzhou.cashflow.ui.profile

import androidx.compose.foundation.Image
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
import com.zhengzhou.cashflow.BottomOptionCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ui.BottomNavigationBar

@Preview
@Composable
private fun ProfileScreenPreview(){
    ProfileScreen(
        bottomOptionCurrentScreen = BottomOptionCurrentScreen.Profile,
        setBottomOptionCurrentScreen = { },
        navController = rememberNavController()
    )
}

@Composable
fun ProfileScreen(
    bottomOptionCurrentScreen: BottomOptionCurrentScreen,
    setBottomOptionCurrentScreen: (BottomOptionCurrentScreen) -> Unit,
    navController: NavController,
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val profileUiState by profileViewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            ProfileTopAppBar()
        },
        content = {

        },
        bottomBar = {
            BottomNavigationBar(
                bottomOptionCurrentScreen = bottomOptionCurrentScreen,
                setBottomOptionCurrentScreen = setBottomOptionCurrentScreen,
                navController = navController,
            )
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopAppBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.profile))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    // TODO navigation
                },
                content = {
                    Image(
                        painter  = painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(id = R.string.accessibility_menu_navbar),
                    )
                },
                modifier = modifier
            )
        }
    )
}