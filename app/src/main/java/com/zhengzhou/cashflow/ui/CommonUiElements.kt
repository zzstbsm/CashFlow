package com.zhengzhou.cashflow.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.zhengzhou.cashflow.BottomOptionCurrentScreen

@Composable
fun BottomNavigationBar(
    bottomOptionCurrentScreen: BottomOptionCurrentScreen,
    setBottomOptionCurrentScreen: (BottomOptionCurrentScreen) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    NavigationBar() {
        BottomOptionCurrentScreen.elements.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = stringResource(id = item.accessibilityText),
                    )
                },
                label = {
                    Text(stringResource(item.optionName))
                },
                selected = (bottomOptionCurrentScreen == item),
                onClick = {
                    setBottomOptionCurrentScreen(item)
                    item.navigate(navController = navController)
                }
            )
        }
    }
}