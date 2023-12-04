package com.zhengzhou.cashflow.transaction_edit


/*
@Composable
internal fun ChooseSecondaryWallet(
    transactionEditUiState: TransactionEditUiState,
    transactionEditViewModel: TransactionEditViewModel,
    modifier: Modifier = Modifier,
) {

    val walletList by viewModel.walletsList.collectAsState()

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Primary wallet
        WalletCardDropdown(
            wallet = uiState.wallet,
            walletList = walletList,
            onChoice = { chosenWallet ->
                viewModel.setWallet(chosenWallet)
            },
            modifier = modifier,
        )
        
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_down),
            contentDescription = null,
        )
        
        // Secondary wallet
        WalletCardDropdown(
            wallet = uiState.secondaryWallet,
            walletList = walletList,
            onChoice = { chosenWallet ->
                viewModel.setSecondaryWallet(chosenWallet)
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun WalletCardDropdown(
    wallet: Wallet,
    walletList: MutableList<Wallet>,
    onChoice: (Wallet) -> Unit,
    modifier: Modifier = Modifier,
) {

    val horizontalPaddingDP = 32.dp

    val buttonModifier = modifier
        .height(64.dp)
        .fillMaxWidth()
    val surfaceModifier = buttonModifier
        .padding(horizontal = horizontalPaddingDP, vertical = 4.dp)

    var walletListOpen by remember { mutableStateOf(false) }

    Surface(
        modifier = surfaceModifier,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        OutlinedButton(
            onClick = { walletListOpen = true },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.dp, Color.White),
            modifier = buttonModifier,
        ) {
            // TODO content of the button
            Icon(
                painter = painterResource(
                    id = wallet.iconId,
                ),
                contentDescription = null,
            )
            Text(
                text = wallet.name,
            )
        }
    }

    DropdownMenu(
        expanded = walletListOpen,
        onDismissRequest = {
            walletListOpen = false
        },
        offset = DpOffset(horizontalPaddingDP,0.dp),
        modifier = modifier
            .fillMaxWidth(.5f)
            .padding(horizontal = 8.dp),
    ) {
        walletList.forEach {walletInMenu: Wallet ->
            DropdownMenuItem(
                text = {
                    Row{
                        Icon(
                            painter = painterResource(walletInMenu.iconId),
                            contentDescription = null // TODO: add description
                        )
                        Spacer(modifier = modifier.width(4.dp))
                        Text(walletInMenu.name)
                    }
                },
                onClick = {
                    walletListOpen = false
                    onChoice(walletInMenu)
                }
            )
        }
    }
}

 */