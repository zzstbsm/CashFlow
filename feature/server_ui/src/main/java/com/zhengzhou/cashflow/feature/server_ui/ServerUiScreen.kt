package com.zhengzhou.cashflow.feature.server_ui

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.feature.server_ui.view_model.ServerActions
import com.zhengzhou.cashflow.feature.server_ui.view_model.ServerEvent
import com.zhengzhou.cashflow.feature.server_ui.view_model.ServerUiState
import com.zhengzhou.cashflow.feature.server_ui.view_model.ServerUiViewModel
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.navigation.functions.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.tools.ui_elements.navigation.BottomNavigationBar
import com.zhengzhou.cashflow.tools.ui_elements.navigation.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.tools.ui_elements.navigation.SectionTopAppBar

@Composable
fun ServerUiScreen(
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    navController: NavController,
    context: Context,
) {

    val serverUiViewModel: ServerUiViewModel = viewModel {
        ServerUiViewModel(
            connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
    }
    val serverUiState by serverUiViewModel.uiState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ReloadPageAfterPopBackStack(
        pageRoute = Screen.Profile.route,
        navController = navController,
    ) {
        setCurrentScreen(ApplicationScreensEnum.Profile)
    }

    ModalNavigationDrawer(
        drawerContent = {
            SectionNavigationDrawerSheet(
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
                SectionTopAppBar(
                    currentScreen = currentScreen,
                    drawerState = drawerState,
                )
            },
            content = { paddingValues ->
                ScreenProfileMainBody(
                    serverUiState = serverUiState,
                    serverUiViewModel = serverUiViewModel,
                    modifier = Modifier.padding(paddingValues)
                )
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

@Composable
private fun ScreenProfileMainBody(
    serverUiState: ServerUiState,
    serverUiViewModel: ServerUiViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {

        if (serverUiState.serverActive) {

            val text = serverUiState.ipAddress ?: "WIP"

            Text(text = text)
        }
        Switch(
            checked = serverUiState.serverActive,
            onCheckedChange = { newState ->
                when (newState) {
                    true -> {
                        serverUiViewModel.onEvent(
                            event = ServerEvent.SetServerActivationState(
                                ServerActions.Activate
                            )
                        )
                    }
                    false -> {
                        serverUiViewModel.onEvent(
                            event = ServerEvent.SetServerActivationState(
                                ServerActions.Deactivate
                            )
                        )
                    }
                }
            }
        )
    }
}
