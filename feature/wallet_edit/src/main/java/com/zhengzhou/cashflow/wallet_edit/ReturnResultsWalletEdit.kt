package com.zhengzhou.cashflow.wallet_edit

internal enum class WalletEditSaveResults(
    val message: Int,
) {
    SUCCESS(
        message = R.string.wallet_saved
    ),
    NON_VALID_NAME(
        message = R.string.error_wallet_name_not_valid
    ),
    NON_VALID_AMOUNT(
        message = R.string.error_amount_non_valid
    ),
    NAME_IN_USE(
        message = R.string.error_wallet_name_already_in_use
    )
}