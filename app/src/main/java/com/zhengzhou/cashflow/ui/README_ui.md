# UI screen default structure
The default structure of a page is the following:
```
@Composable
fun DefaultScreen(
    *args: ,
    currentScreen: ApplicationScreensEnum,
    setCurrentScreen: (ApplicationScreensEnum) -> Unit,
    navController: NavController
) {

    val screenViewModel: ViewModel = viewModel {
        ScreenViewModel(
            *args
        )
    }
    val screenUiState by screenViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            SectionNavigationDrawerSheet(
                drawerState = drawerState,
                currentScreen = currentScreen,
                setCurrentScreen = setCurrentScreen,
                navController = navController,
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
                    actions = {
                        ScreenAppBarAction(
                            screenUiState = screenUiState,
                            screenViewModel = screenViewModel,
                            navController = navController,
                        )
                    }
                )
            },
            content = { innerPadding ->
                ScreenMainBody(
                    screenUiState = screenUiState,
                    screenViewModel = screenViewModel,
                    innerPadding = innerPadding,
                    navController = navController,
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    currentScreen = currentScreen,
                    setCurrentScreen = setCurrentScreen,
                    navController = navController
                )
            },
            floatingActionButton = {
                ScreenFloatingActionButton(
                    screenUiState = screenUiState,
                    screenViewModel = screenViewModel,
                    navController = navController,
                )
            }
        )
    }
}
```