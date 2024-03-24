package com.zhengzhou.cashflow.navigation.functions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

@Composable
fun ReloadPageAfterPopBackStack(
    pageRoute:String,
    navController: NavController,
    onPopBackStack: () -> Unit,
) {
    val callback = NavController.OnDestinationChangedListener { _, destination, _ ->
        if (destination.route == pageRoute) {
            onPopBackStack()
        }
    }
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener(callback)
    }
    DisposableEffect(Unit) {
        onDispose {
            navController.removeOnDestinationChangedListener(callback)
        }
    }
}