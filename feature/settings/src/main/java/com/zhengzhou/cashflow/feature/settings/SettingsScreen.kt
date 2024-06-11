package com.zhengzhou.cashflow.feature.settings

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.feature.settings.components.SingleOptionTile
import com.zhengzhou.cashflow.feature.settings.view_model.SettingsEvents
import com.zhengzhou.cashflow.feature.settings.view_model.SettingsList
import com.zhengzhou.cashflow.feature.settings.view_model.SettingsState
import com.zhengzhou.cashflow.feature.settings.view_model.SettingsViewModel
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.navigation.functions.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.tools.accessToSharedStorage.handleLoadJsonResult
import com.zhengzhou.cashflow.tools.accessToSharedStorage.handleSaveJsonResult
import com.zhengzhou.cashflow.tools.accessToSharedStorage.LoadJsonWithSAF
import com.zhengzhou.cashflow.tools.accessToSharedStorage.SaveJsonWithSAF
import com.zhengzhou.cashflow.tools.ui_elements.navigation.BottomNavigationBar
import com.zhengzhou.cashflow.tools.ui_elements.navigation.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.tools.ui_elements.navigation.SectionTopAppBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val TAG = "SettingsScreen"

@Composable
fun SettingsScreen(
    context: Context,
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    navController: NavController,
) {

    val settingsViewModel: SettingsViewModel = viewModel{
        SettingsViewModel()
    }
    val settingsState by settingsViewModel.uiState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ReloadPageAfterPopBackStack(
        pageRoute = Screen.Settings.route,
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
                SettingsScreenMainBody(
                    context = context,
                    settingsState = settingsState,
                    settingsViewModel = settingsViewModel,
                    modifier = Modifier.padding(paddingValues),
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
private fun SettingsScreenMainBody(
    context: Context,
    settingsState: SettingsState,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
) {

    val handleSaveJsonResult = handleSaveJsonResult(
        context = context,
        coroutineScope = rememberCoroutineScope(),
        jsonData = { "{}" } // TODO: Implement save data, replace with a function that retrieves the actual json data
    )
    val handleLoadJsonResult = handleLoadJsonResult(
        context = context,
        coroutineScope = rememberCoroutineScope(),
        onJsonLoaded = { json ->
            EventMessages.sendMessage(
                json
            )
        }
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        item {
            SingleOptionTile(
                title = stringResource(SettingsList.Backup.title),
                description = SettingsList.Backup.description?.let {
                    stringResource(it)
                },
                icon = SettingsList.Backup.icon,
                onClick = {
                    settingsViewModel.onEvent(SettingsEvents.ActivateTriggerSave)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            SingleOptionTile(
                title = stringResource(SettingsList.Restore.title),
                description = SettingsList.Restore.description?.let {
                    stringResource(it)
                },
                icon = SettingsList.Restore.icon,
                onClick = {
                    settingsViewModel.onEvent(SettingsEvents.ActivateTriggerLoad)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (settingsViewModel.accessSaveTrigger()) {
        Log.d(TAG, "Save triggered")
        val timestampFormat = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault())
        val currentDateTime = timestampFormat.format(Date())


        // Handle save data
        SaveJsonWithSAF(
            fileName = "Cashflow_backup - $currentDateTime",
            launcher = handleSaveJsonResult,
        )
    }

    if (settingsViewModel.accessLoadTrigger()) {
        Log.d(TAG, "Load triggered")
        // Handle load data

        LoadJsonWithSAF(
            launcher = handleLoadJsonResult,
        )

    }
}
