# Data classes in database

## Budget

`Budget` holds all the information regarding the budgeting of a wallet and it is derived by two other data classes: `BudgetPerCategory` and `BudgetPerPeriod`.
The UI interacts with `Budget` only, while `BudgetPerCategory` and `BudgetPerPeriod` are only tables in the database, the usage can be found below.
Every time a ViewModel needs to retrieve the budget data, the database retrieves the corresponding `budgetPerCategory` and `budgetPerPeriod` and merges them together, the reverse is done when adding or updating a budget entry.

Variables in `Budget`:
- `id`: UUID of the corresponding `BudgetPerPeriod`.
- `budgetPerCategoryUUID`: id of the corresponding `BudgetPerCategory`, if `budgetPerCategoryUUID == UUID(0L,0L)`.
- `categoryUUID`: id of the category associated to the `BudgetPerCategory`; if `categoryUUID == UUID(0L,0L)` it the entry refers to the max cap of the wallet.
- `currency`:
- `endDate`:
- `maxAmount`:
- `startDate`:
- `walletUUID`:

### `BudgetPerCategory`

If the budget is active in a certain period, each category has his own `BudgetPerCategory` entry that is linked to the wallet, period and category.

### `BudgetPerPeriod`

If a wallet has the budgeting active, this entry set the time period in which the budget should be considered.

## Category

Variables:
- `id`: id of the category.
- `iconName`: name of the icon of the transaction, it is an object of the type `IconsMappedForDB` since these are the icons that can be associated to a database object.
- `name`: name of the category.
- `transactionType`: it indicates the type of the transaction that can be associated with this category.

## Location

Documentation to add.

## Tag

`Tag` holds all the information regarding one tag of a transaction.
It is constructed using two other data classes: `TagEntry` and `TagTransaction`.
The UI interacts with `Tag` only, while `TagEntry` and `TagTransaction` are only tables in the database, the usage can be found below.
Every time a ViewModel needs to retrieve a tag, the database retrieves the corresponding `tagEntry` and `tagTransaction` and merges them together, the reverse is done when adding or updating a tag.

Variables in `Tag`:
- `id`: id of the tag in `TagTransaction`.
- `count`: times that the tag appears in the DB.
- `enabled`: if `true` the tag should be added to the db or the `count` should be increased, otherwise the tag should not be added in the db or `count` should be decreased; if `count` reaches zero, the tag should be deleted from the db.
- `name`: name of the tag.
- `tagUUID`: id of the tag in `TagEntry`.
- `transactionUUID`: id of the transaction that have the current tag, more tags can be associated to the same transaction.

### `TagEntry`

Since multiple transactions can have the same tag, they are stored in the database without repeating twice the name of the tag in a hypothetical `tag` table.

Variables in `TagEntry`:
- `id`: id of the tag.
- `count`: counts the number of transactions with the same tag, when `count == 0` the entry is deleted from the database.
- `name`: name of the tag.

### `TagTransaction`

This table links a tag with the transaction.

Variables in `TagTransaction`:
- `id`: id of the entry in the database.
- `tagUUID`: id of the tag in the `TagEntry` table.
- `transactionUUID`: id of the transaction with the tag.

## Transaction

`Transaction` is the class that holds all the data of one transaction.

Variables:
- `id`: id of the transaction.
- `amount`: amount of the transaction, deposit if removal from and expense if addition to the wallet.
- `date`: date of the transaction.
- `categoryUUID`: id of the category of the transaction.
- `description`: description of the category, it won't be used for filtering.
- `isBlueprint`: the entry is `true` if the transaction is a model and it won't be used to compute the total amount in a wallet.
- `locationUUID`: id of the entry that sets the location of the expense.
- `secondaryWalletUUID`: it should be compiled only if the transaction is a transfer from one wallet to another, it indicates the id of the wallet that receives the transaction.
- `transactionType`: it indicates if the transaction is deposit, expense or movement (transfer of money from one wallet to another).
- `walletUUID`: id of the primary wallet of the transaction.

## Wallet

Documentation to add.

# Enum classes

## Currency

Documentation to add.

## TransactionType

Documentation to add.