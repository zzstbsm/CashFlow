package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.tools.KeypadDigit
import java.util.UUID

@Composable
fun TransactionEditScreen(
    walletUUID: UUID,
    transactionType: TransactionType,
    transactionUUID: UUID,
    navController: NavController,
) {

    val transactionEditViewModel: TransactionEditViewModel = viewModel {
        TransactionEditViewModel(
            walletUUID = walletUUID,
            transactionUUID = transactionUUID,
            transactionType = transactionType,
        )
    }
    val transactionEditUiState by transactionEditViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TransactionEditTopAppBar(
                transactionType = transactionType,
                navController = navController,
            )
        },
        content = { paddingValues ->
            TransactionEditMainBody(
                transactionEditUiState = transactionEditUiState,
                transactionEditViewModel = transactionEditViewModel,
                transactionType = transactionType,
                contentPadding = paddingValues,
            )
        },
        floatingActionButton = {
            if (!transactionEditUiState.isLoading) {
                FloatingActionButton(
                    onClick = {
                        transactionEditViewModel.saveTransaction()
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = stringResource(
                            id = R.string.TransactionEdit_save_transaction
                        )
                    )
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditTopAppBar(
    transactionType: TransactionType,
    navController: NavController,
) {
    TopAppBar(
        title = {
            Text(
                stringResource(id = transactionType.new_text)
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = stringResource(id = R.string.nav_back)
                )
            }
        }
    )
}

@Composable
fun TransactionEditMainBody(
    transactionEditUiState: TransactionEditUiState,
    transactionEditViewModel: TransactionEditViewModel,
    transactionType: TransactionType,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(contentPadding)
    ) {
        // Choose date
        ChooseDateSection(
            date = transactionEditUiState.transaction.date,
            onSetNewDate= { newDate ->
                transactionEditViewModel.updateTransactionDate(newDate)
            }
        )
        // Amount text
        AmountTextSection(
            amountString = transactionEditUiState.amountString,
            wallet = transactionEditUiState.wallet,
            walletList = transactionEditUiState.walletList,
            onWalletSelected = { selectedWallet ->
                transactionEditViewModel.updateWallet(wallet = selectedWallet)
            },
            onBackKeyClick = { transactionEditViewModel.amountKeyPress(KeypadDigit.KeyBack) }
        )
        DescriptionSection(
            text = transactionEditUiState.transaction.description,
            onTextChange = { newText ->
                transactionEditViewModel.updateDescription(newText)
            },
            modifier = modifier
        )

        if (
            transactionEditUiState.transaction.movementType == TransactionType.Deposit.id
            ||
            transactionEditUiState.transaction.movementType == TransactionType.Expense.id
        ) {
            TransactionMovementTypeTab(
                transactionEditUiState = transactionEditUiState,
                transactionEditViewModel = transactionEditViewModel,
                modifier = modifier,
            )
        }

        /*
     else {
        MovementSubsection(
            uiState = uiState,
            viewModel = viewModel,
            modifier = modifier,
        )

        }
         */
    }
}

@Composable
private fun TransactionMovementTypeTab(
    transactionEditUiState: TransactionEditUiState,
    transactionEditViewModel: TransactionEditViewModel,
    modifier: Modifier = Modifier
) {

    var activeTab by remember {
        mutableIntStateOf(0)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TabRow(selectedTabIndex = activeTab) {
            TransactionEditScreenToChooseFunctionality.forEachIndexed { index, section ->
                Tab(
                    selected = activeTab == index,
                    onClick = {
                        activeTab = index
                        transactionEditViewModel.setChooseFunctionality(section)
                    },
                    text = { Text(text = stringResource(id = section.text)) },
                    icon = {
                        Icon(
                            painter = painterResource(id = section.icon),
                            contentDescription = stringResource(id = section.text)
                        )
                    }
                )
            }
        }
        TransferMovementTypeSection(
            transactionEditUiState = transactionEditUiState,
            transactionEditViewModel = transactionEditViewModel,
            modifier = Modifier,
        )
    }
}

@Composable
private fun TransferMovementTypeSection(
    transactionEditUiState: TransactionEditUiState,
    transactionEditViewModel: TransactionEditViewModel,
    modifier: Modifier = Modifier
) {

    // Choice section
    when(transactionEditUiState.transactionSectionToShow) {
        TransactionSectionToShow.AMOUNT -> {
            KeypadTransactionSection(
                onDigitClick = { pressedKey ->
                    transactionEditViewModel.amountKeyPress(pressedKey)
                }
            )
        }
        TransactionSectionToShow.CATEGORY -> {
            ChooseCategorySection(
                categoryList = transactionEditUiState.categoryList,
                currentlyChosenCategoryId = transactionEditUiState.transaction.idCategory,
                onCategoryChoice = { category ->
                    transactionEditViewModel.updateCategory(category)
                }
            )
        }
        TransactionSectionToShow.TAG -> {

            var currentTagText by remember { mutableStateOf("") }
            TagSection(
                transactionTagList = transactionEditUiState.transactionTagList,
                tagList = transactionEditUiState.tagListInDB,
                currentTagText = currentTagText,
                onChangeText = { currentTagText = it },
                onTagAdd = { transactionEditViewModel.addTag(it) },
                onTagRemove = { transactionEditViewModel.removeTag(it) }
            )
        }
        else -> {
            Text(text = "Not yet implemented")
        }
    }
}

/*

@Composable
private fun ChoiceButton(
    currentChoice: OperationScreenToShow,
    viewModel: OperationViewModel,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            viewModel.uiSelectToShowSection(currentChoice)
        },
        modifier = modifier.padding(horizontal = 2.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = currentChoice.icon),
                contentDescription = null, // TODO: add description
            )
            Text(
                text = stringResource(id = currentChoice.text)
            )
        }
    }
}

@Composable
private fun ExpenseOrDepositSubsection(
    uiState: OperationViewModel.UiState,
    viewModel: OperationViewModel,
    modifier: Modifier = Modifier,
) {
    // Select function to choose
    LazyRow(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        items(OperationScreenToChooseFunctionality.size) {pos ->
            ChoiceButton(
                currentChoice = OperationScreenToChooseFunctionality[pos],
                viewModel = viewModel,
                modifier = modifier
            )
        }
    }

    // Choice section
    when(uiState.toShow) {
        OperationScreenToShow.AMOUNT -> {
            KeypadTransactionSection(
                onDigitClick = { pressedKey ->
                    viewModel.keyPressed(pressedKey)
                }
            )
        }
        OperationScreenToShow.CATEGORY -> {
            ChooseCategorySection(
                uiState = uiState,
                viewModel = viewModel,
            )
        }
        OperationScreenToShow.TAG -> {
            TagSection(
                uiState = uiState,
                viewModel = viewModel,
                modifier = modifier,
            )
        }
        else -> {
            Text(text = "Not yet implemented")
        }
    }
}

@Composable
private fun MovementSubsection(
    uiState: OperationViewModel.UiState,
    viewModel: OperationViewModel,
    modifier: Modifier = Modifier,
) {

    ChoiceButton(
        currentChoice = OperationScreenToShow.MOVEMENT,
        viewModel = viewModel,
        modifier = modifier
    )

    // Choice section
    when(uiState.toShow) {
        OperationScreenToShow.AMOUNT -> {
            KeypadTransactionSection(
                onDigitClick = { pressedKey ->
                    viewModel.keyPressed(pressedKey)
                }
            )
        }
        OperationScreenToShow.MOVEMENT -> {
            ChooseSecondaryWallet(
                uiState = uiState,
                viewModel = viewModel,
                modifier = modifier,
            )
        }
        else -> {
            Text(text = "Not yet implemented")
        }
    }
}

 */