package com.zhengzhou.cashflow.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.ui.BottomNavigationBar
import com.zhengzhou.cashflow.ui.CustomNavigationDrawerSheet
import kotlinx.coroutines.launch

@Preview
@Composable
private fun ProfileScreenPreview(){
    ProfileScreen(
        currentScreen = NavigationCurrentScreen.Profile,
        setCurrentScreen = { },
        navController = rememberNavController()
    )
}

@Composable
fun ProfileScreen(
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController,
) {
    val profileViewModel: ProfileViewModel = viewModel()
    val profileUiState by profileViewModel.uiState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            CustomNavigationDrawerSheet(
                drawerState = drawerState,
                currentScreen = currentScreen,
                setCurrentScreen = setCurrentScreen,
                navController = navController
            )
        },
        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
        drawerState = drawerState,
    ) {
        Scaffold (
            topBar = {
                ProfileTopAppBar(
                    drawerState = drawerState,
                )
            },
            content = {

            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    setCurrentScreen = setCurrentScreen,
                    navController = navController,
                )
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopAppBar(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
) {

    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.profile))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
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