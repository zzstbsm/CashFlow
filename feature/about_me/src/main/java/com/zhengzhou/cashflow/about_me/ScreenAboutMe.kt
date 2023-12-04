package com.zhengzhou.cashflow.about_me

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.navigation.ApplicationScreensEnum
import com.zhengzhou.cashflow.navigation.Screen
import com.zhengzhou.cashflow.navigation.functions.ReloadPageAfterPopBackStack
import com.zhengzhou.cashflow.themes.ui_elements.navigation.SectionNavigationDrawerSheet
import com.zhengzhou.cashflow.themes.ui_elements.navigation.SectionTopAppBar

@Composable
fun AboutMeScreen(
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    navController: NavController
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showMakeDonationDialog by remember { mutableStateOf(false) }

    ReloadPageAfterPopBackStack(
        pageRoute = Screen.Balance.route,
        navController = navController,
    ) {
        setCurrentScreen(ApplicationScreensEnum.Balance)
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
        Scaffold(
            topBar = {
                SectionTopAppBar(
                    currentScreen = currentScreen,
                    drawerState = drawerState,
                )
            },
            content = { innerPadding ->
                AboutMeMainBody(
                    showMakeDonationDialog = showMakeDonationDialog,
                    onDismissDialog = { showMakeDonationDialog = false },
                    innerPaddingValues = innerPadding,
                )
            },
            floatingActionButton = {
                AboutMeFloatingActionButton(
                    onClick = {
                        showMakeDonationDialog = true
                    }
                )
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutMeMainBody(
    showMakeDonationDialog: Boolean,
    onDismissDialog: () -> Unit,
    innerPaddingValues: PaddingValues,
) {

    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp)

    val localUri = LocalUriHandler.current

    Column(
        modifier = Modifier.padding(innerPaddingValues)
    ) {
        Text(
            text = stringResource(id = R.string.description),
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(16.dp))

        ClickableDescription(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.logo_github),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            },
            text = AboutMeAnnotatedStrings.GITHUB.getAnnotatedString(),
            onClick = { clickPosition ->
                AboutMeAnnotatedStrings.GITHUB.onClick(
                    localUri,
                    clickPosition,
                    clickPosition
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ClickableDescription(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.logo_linkedin),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            },
            text = AboutMeAnnotatedStrings.LINKEDIN.getAnnotatedString(),
            onClick = { clickPosition ->
                AboutMeAnnotatedStrings.LINKEDIN.onClick(
                    localUri,
                    clickPosition,
                    clickPosition
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }

    if (showMakeDonationDialog) {
        BasicAlertDialog(onDismissRequest = onDismissDialog) {
            Card(
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "\u2764 " + stringResource(id = R.string.donate),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = stringResource(id = R.string.donate_dialog))
                }
            }
        }
    }
}

@Composable
private fun ClickableDescription(
    icon: @Composable () -> Unit,
    text: AnnotatedString,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        icon()
        Spacer(modifier = Modifier.width(16.dp))
        ClickableText(
            text = text,
            onClick = onClick
        )
    }
}

@Composable
private fun AboutMeFloatingActionButton(
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        text = {
            Text(text = stringResource(id = R.string.donate))
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = stringResource(id = R.string.donate),
                modifier = Modifier.size(32.dp)
            )
        },
        onClick = onClick,
    )
}