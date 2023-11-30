# Navigation documentation

This page describes how the navigation in the application is handled, what are the pages and what are the parameters accepted by each screen.

## Navigation functions

### `NavigationApp`
The function `NavigationApp` handles the composition of the screens, it has the variable `currentScreen` that highlights the current page in the navigation drawer and the navigation bottom bar.

### `BackHandler`
The function `BackHandler` manages the go back button in a screen. If called, it is enabled by default.
```
fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit,
)
```
The `onBack` argument takes a lambda that handles the actions to perform instead of popping back the stack.

### `ReloadPageAfterPopBackStack`
After a `navigationController.popBackStack()`, a recomposition of the screen is sometimes required. The function forces the recomposition itself and loads the most recent data on the screen.

## How to add pages

### Add new page in the navigation bar
In order to add a new page in the navigation bar, it is needed to add page in `NavigationCurrentScreen` with `navBarActive = true`

If the navigation path is not ready yet, set also the parameter `routeActive = false` in order to make the screen non navigable.
After the page has been set ready for the navigation, remove the parameter.

The order of the element in the enum list determines the order in the navigation bar.

In order to set the page as navigable, put a new object in the `Screen` class. Then make it callable in `NavigationApp`

### Add new page in the navigation bottom bar
Do the same as in the navigation bar drawer, but by setting `bottomActive = true`.

## Navigable screens

### `Balance`
Shows the total balance in all the wallets with the same currency. The default screen uses the euro.

### `ManageCategories`
Manages the categories saved in the database.

### `Profile`
Manages the profile. So far it is empty.

### `WalletEdit`
Screen that handles the initialization of a new wallet or the edit of an old one. One can select the icon, name, initial amount in the wallet, creation date, currency and set the budget.

### `WalletOverview`
Shows all the details of a single wallet with a short list of the most recent transactions and the current amount in it.

### `TransactionEdit`
Screen that handles a transition (add or edit) and saves it in the database.

### `TransactionReport`
Screen that summarize one transaction: it contains the description of the transaction, its wallet, category and tags.