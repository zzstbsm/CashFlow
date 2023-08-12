package com.zhengzhou.cashflow.ui.walletEdit

import com.zhengzhou.cashflow.R

enum class WalletEditSaveResults(
    val message: Int,
) {
    SUCCESS(
        message = R.string.WalletEdit_wallet_saved
    ),
    NON_VALID_NAME(
        message = R.string.WalletEdit_error_wallet_name_not_valid
    ),
    NON_VALID_AMOUNT(
        message = R.string.WalletEdit_error_amount_non_valid
    ),
    NAME_IN_USE(
        message = R.string.WalletEdit_error_wallet_name_already_in_use
    )
}