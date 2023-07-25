package com.zhengzhou.cashflow.ui

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.NavigationCurrentScreen
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.tools.KeypadDigit
import com.zhengzhou.cashflow.tools.mapCharToKeypadDigit
import com.zhengzhou.cashflow.tools.mapIconsFromName
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Date

@Composable
fun BottomNavigationBar(
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController,
) {

    NavigationBar {
        NavigationCurrentScreen.elements
            .filter{ screen ->
            screen.bottomActive
            }.forEach { item ->
                NavigationBarItem(
                    icon = {
                        RouteIcon(navigationCurrentScreen = item)
                    },
                    label = {
                        Text(stringResource(item.optionName))
                    },
                    selected = (currentScreen == item),
                    onClick = {
                        routeClick(
                            setCurrentScreen = setCurrentScreen,
                            navigationCurrentScreen = item,
                            navController = navController,
                        )
                    }
                )
            }
    }
}

@Composable
fun SectionNavigationDrawerSheet(
    drawerState: DrawerState,
    currentScreen: NavigationCurrentScreen,
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navController: NavController,
) {

    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Column {
            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp),
            )
            NavigationCurrentScreen.elements
                .filter { item ->
                    item.navBarActive
                }
                .forEach { item ->
                    NavigationDrawerItem(
                        icon = {
                            RouteIcon(navigationCurrentScreen = item)
                        },
                        label = { Text(stringResource(id = item.optionName)) },
                        selected = item == currentScreen,
                        onClick = {
                            scope.launch { drawerState.close() }
                            routeClick(
                                setCurrentScreen = setCurrentScreen,
                                navigationCurrentScreen = item,
                                navController = navController,
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionTopAppBar(
    currentScreen: NavigationCurrentScreen,
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
                modifier = modifier,
            ) {
                Icon (
                    painter  = painterResource(id = R.drawable.ic_menu),
                    contentDescription = stringResource(id = R.string.accessibility_menu_navbar),
                )
            }
        },
        actions = actions
    )
}

@Composable
private fun RouteIcon(
    navigationCurrentScreen : NavigationCurrentScreen,
) {
    Icon(
        painter = painterResource(id = navigationCurrentScreen.iconId),
        contentDescription = if (navigationCurrentScreen.accessibilityText == null) {
            null
        } else {
            stringResource(id = navigationCurrentScreen.accessibilityText)
        }
    )
}

private fun routeClick(
    setCurrentScreen: (NavigationCurrentScreen) -> Unit,
    navigationCurrentScreen: NavigationCurrentScreen,
    navController: NavController
) {
    if (navigationCurrentScreen.routeActive) {
        setCurrentScreen(navigationCurrentScreen)
        navigationCurrentScreen.navigateTab(navController = navController)
    }
    else {
        EventMessages.sendMessage("Route not active")
    }
}

@Composable
fun MoneyTextField(
    label: String,
    amountOnScreen: String,
    onKeyPressed: (KeypadDigit) -> Unit,
    modifier: Modifier = Modifier,
) {

    OutlinedTextField(
        label = {
            Text(text = label)
        },
        value = amountOnScreen,
        onValueChange = { newText ->
            if (newText.length >= amountOnScreen.length) {
                val newDigit = newText.last()
                val newKey: KeypadDigit? = mapCharToKeypadDigit(newDigit)
                if (newKey != null) {
                    onKeyPressed(newKey)
                }
            } else {
                onKeyPressed(KeypadDigit.KeyBack)
            }
        },
        modifier = modifier,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    label: String,
    dateFormat: String,
    date: Date,
    onSelectDate: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    selectableDatesCondition: (Long) -> Boolean = { true },
) {
    var showDatePickerState by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.time,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return selectableDatesCondition(utcTimeMillis)
            }
        },
    )

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        label = {
            Text(text = label)
        },
        value = DateFormat.format(
            dateFormat,
            date
        ).toString(),
        onValueChange = { },
        modifier = modifier
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    showDatePickerState = true
                    focusManager.clearFocus()
                }
            }
        ,
        maxLines = 1,
    )

    if (showDatePickerState) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerState = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSelectDate(datePickerState.selectedDateMillis)
                        showDatePickerState = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm)
                    )
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun SectionTransactionEntry(
    transaction: Transaction,
    category: Category,
    currencyFormatter: NumberFormat,
    onClickTransaction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .height(60.dp)
            .clickable { onClickTransaction() },
        // shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.large,
    ) {
        val firstLineStyle = MaterialTheme.typography.bodyLarge
        val secondLineStyle = MaterialTheme.typography.bodySmall

        Row(
            modifier = modifier
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(
                    id = mapIconsFromName[category.iconName]!!
                ),
                contentDescription = category.name, //TODO add description
                modifier = modifier
                    .size(54.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = category.name,
                        style = firstLineStyle,
                        textAlign = TextAlign.Start,
                        modifier = modifier.weight(1f)
                    )
                    Text(
                        text = currencyFormatter.format(transaction.amount),
                        style = firstLineStyle,
                        textAlign = TextAlign.End,
                        color = if (transaction.amount >= 0) {
                            Color.Green
                        } else {
                            Color.Red
                        }
                    )
                }
                Row {
                    Text(
                        text = DateFormat.format(
                            "MMM dd",
                            transaction.date
                        ).toString(),
                        style = secondLineStyle
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = "-",
                        style = secondLineStyle
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = transaction.description,
                        style = secondLineStyle
                    )
                }
            }
        }
    }
}