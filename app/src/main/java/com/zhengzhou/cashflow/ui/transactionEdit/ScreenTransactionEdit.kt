package com.zhengzhou.cashflow.ui.transactionEdit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.zhengzhou.cashflow.BackHandler
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.data.TransactionType
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.tools.KeypadDigit
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionEditScreen(
    transactionType: TransactionType,
    transactionUUID: UUID,
    isBlueprint: Boolean,
    editBlueprint: Boolean,
    navController: NavController,
) {

    val transactionEditViewModel: TransactionEditViewModel = viewModel {
        TransactionEditViewModel(
            transactionUUID = transactionUUID,
            transactionType = transactionType,
            isBlueprint = isBlueprint,
            editBlueprint = editBlueprint,
        )
    }
    val transactionEditUiState by transactionEditViewModel.uiState.collectAsState()

    var showBackDialog by remember {
        mutableStateOf(false)
    }

    BackHandler {
        showBackDialog = true
    }

    Scaffold(
        topBar = {
            TransactionEditTopAppBar(
                transactionType = transactionType,
                onNavigationIconClick = {
                    showBackDialog = true
                },
            )
        },
        content = { paddingValues ->
            TransactionEditMainBody(
                transactionEditUiState = transactionEditUiState,
                transactionEditViewModel = transactionEditViewModel,
                contentPadding = paddingValues,
            )
            
            if (showBackDialog) {
                AlertDialog(onDismissRequest = { showBackDialog = false }) {
                    Card() {
                        Text(
                            text = stringResource(id = R.string.TransactionEdit_prompt_save_transaction),
                            modifier = Modifier.padding(16.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(
                                onClick = {
                                    navController.popBackStack()
                                    showBackDialog = false
                                }
                            ) {
                                Text(
                                    stringResource(id = R.string.no)
                                )
                            }
                            TextButton(
                                onClick = {
                                    val saveTransactionResult = transactionEditViewModel.saveTransaction()
                                    val saveTransactionSuccessful = saveTransactionCheck(saveTransactionResult)
                                    if (saveTransactionSuccessful) navController.popBackStack()
                                    showBackDialog = false
                                }
                            ) {
                                Text(
                                    stringResource(id = R.string.yes)
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (!transactionEditUiState.isLoading) {
                FloatingActionButton(
                    onClick = {
                        val saveTransactionResult = transactionEditViewModel.saveTransaction()
                        val saveTransactionSuccessful = saveTransactionCheck(saveTransactionResult)
                        if (saveTransactionSuccessful) navController.popBackStack()
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
    onNavigationIconClick: (Boolean) -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                stringResource(id = transactionType.newText)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onNavigationIconClick(true) }
            ) {
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
            transactionEditUiState.transaction.movementType == TransactionType.Deposit
            ||
            transactionEditUiState.transaction.movementType == TransactionType.Expense
        ) {
            TransactionMovementTypeTab(
                transactionEditUiState = transactionEditUiState,
                transactionEditViewModel = transactionEditViewModel,
            )
        }
    }
}

@Composable
private fun TransactionMovementTypeTab(
    transactionEditUiState: TransactionEditUiState,
    transactionEditViewModel: TransactionEditViewModel,
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
        )
    }
}

@Composable
private fun TransferMovementTypeSection(
    transactionEditUiState: TransactionEditUiState,
    transactionEditViewModel: TransactionEditViewModel,
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
                currentTagList = transactionEditUiState.currentTagList,
                completeTagList = transactionEditUiState.tagListInDB,
                currentTagText = currentTagText,
                onChangeText = { currentTagText = it },
                onTagAdd = { transactionEditViewModel.addTag(it) },
                onTagClick = {
                    val selectedTag = transactionEditUiState.currentTagList[it]
                    if (selectedTag.enabled) {
                        transactionEditViewModel.disableTag(it)
                    } else {
                        transactionEditViewModel.enableTag(it)
                    }
                    currentTagText = "a"
                    currentTagText = ""
                }
            )
        }
    }
}

private fun saveTransactionCheck(status: TransactionSaveResult): Boolean {

    if (status == TransactionSaveResult.SUCCESS) {
        EventMessages.sendMessageId(R.string.TransactionEdit_transaction_saved)
        return true
    }
    if (status == TransactionSaveResult.NO_AMOUNT) {
        EventMessages.sendMessageId(R.string.TransactionEdit_no_amount)
    }
    if (status == TransactionSaveResult.NO_CATEGORY) {
        EventMessages.sendMessageId(R.string.TransactionEdit_no_category)
    }
    return false

}