package com.zhengzhou.cashflow.customUiElements

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.zhengzhou.cashflow.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionTopAppBar(
    currentScreen: com.zhengzhou.cashflow.navigation.NavigationCurrentScreen,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    pageName: String? = null,
    actions: @Composable RowScope.() -> Unit = { },
) {

    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(text = pageName ?: stringResource(id = currentScreen.optionName))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                modifier = modifier.testTag(
                    com.zhengzhou.cashflow.navigation.NavigationAppTestTag.TAG_OPEN_NAV_DRAWER
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = stringResource(id = R.string.accessibility_menu_navbar),
                )
            }
        },
        actions = actions
    )
}