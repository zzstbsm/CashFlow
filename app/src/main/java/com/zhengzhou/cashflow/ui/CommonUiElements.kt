package com.zhengzhou.cashflow.ui

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Tag
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.navigation.NavigationAppTestTag
import com.zhengzhou.cashflow.navigation.NavigationCurrentScreen
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.tools.IconsMappedForDB
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Date
import java.util.UUID

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
                        Text(stringResource(item.optionNameShort))
                    },
                    selected = (currentScreen == item),
                    onClick = {
                        routeClick(
                            setCurrentScreen = setCurrentScreen,
                            navigationCurrentScreen = item,
                            navController = navController,
                        )
                    },
                    modifier = Modifier.testTag(
                        NavigationAppTestTag.bottomNavBar(item.route)
                    )
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
                        modifier = Modifier
                            .testTag(
                                NavigationAppTestTag.drawerNavBar(item.route)
                            )
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
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
                modifier = modifier.testTag(
                    NavigationAppTestTag.TAG_OPEN_NAV_DRAWER
                ),
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
            CategoryIcon(
                iconName = category.iconName,
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

@Composable
fun CategoryIcon(
    iconName: IconsMappedForDB,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = iconName.resource),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

@Composable
fun DropdownTextFieldMenu(
    label: String,
    value: String,
    enabled: Boolean = true,
    expanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    dropdownMenuContent: @Composable (ColumnScope.() -> Unit),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {

    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            label = {
                Text(text = label)
            },
            value = value,
            onValueChange = { },
            enabled = enabled,
            modifier = Modifier
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        onChangeExpanded(true)
                        focusManager.clearFocus()
                    }
                }
            ,
            maxLines = 1,
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onChangeExpanded(false)
            },
            content = dropdownMenuContent
        )
    }
}

@Composable
fun TagListLazyStaggeredHorizontalGrid(
    tagList: List<Tag>,
    modifier: Modifier = Modifier,
) {
    LazyHorizontalStaggeredGrid(
        rows = StaggeredGridCells.Fixed(1),
        content = {
            if (tagList.isEmpty()) {
                item {
                    SingleTag(
                        tag = "",
                        selected = true,
                        onTagClick = { },
                    )
                }
            } else {
                items(tagList.size) { position ->
                    val tag = tagList[position]
                    SingleTag(
                        tag = tag.name,
                        selected = true,
                        onTagClick = { },
                    )
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleTag(
    tag: String = "",
    selected: Boolean,
    onTagClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (tag == "") {
            Text(
                text = stringResource(id = R.string.tag_no),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            FilterChip(
                selected = selected,
                onClick = { onTagClick() },
                label = { Text(text = tag) },
                leadingIcon =  {
                    if (selected) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check),
                            contentDescription = null,
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear),
                            contentDescription = stringResource(R.string.delete_tag,tag),
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SelectIconsGrid(
    gridColumns: Int,
    categoryList: List<Category>,
    currentlyChosenCategoryId: UUID,
    onCategoryChoice: (Category) -> Unit,
    modifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns),
        modifier = modifier
    ) {

        items(categoryList.size) {position ->

            val itemCategory = categoryList[position]

            if (currentlyChosenCategoryId != itemCategory.id) {
                OutlinedButton(
                    onClick = {
                        onCategoryChoice(itemCategory)
                    },
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp),
                ) {
                    ChooseCategoryButton(
                        category = itemCategory
                    )
                }
            } else {
                Button(
                    onClick = { },
                    modifier = buttonModifier,
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp),
                    enabled = false,
                ) {
                    ChooseCategoryButton(
                        category = itemCategory
                    )
                }
            }
        }
    }
}
@Composable
private fun ChooseCategoryButton(
    category: Category
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CategoryIcon(
            iconName = category.iconName,
            contentDescription = null, // TODO: add content description
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconChoiceDialog(
    text: String,
    iconList: List<IconsMappedForDB>,
    onDismissRequest: () -> Unit,
    currentSelectedIcon: IconsMappedForDB?,
    onChooseIcon: (IconsMappedForDB) -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(8.dp)
            )

            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(iconList.size) { position ->
                    val icon = iconList[position]
                    OutlinedButton(
                        enabled = icon != currentSelectedIcon,
                        onClick = {
                            onChooseIcon(icon)
                            onDismissRequest()
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .testTag(icon.name),
                    ) {
                        CategoryIcon(
                            iconName = icon,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}