package com.zhengzhou.cashflow.wallet_edit.view_model

import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.themes.icons.IconsMappedForDB
import java.util.Date

internal sealed class WalletEditEvent {

    /**
     * Update the amount of the wallet based on the inserted string
     * @param amount in string
     */
    data class UpdateAmount(val amount: String): WalletEditEvent()

    /**
     * Update the data regarding the wallet being edited
     * @param name: name of the wallet
     * @param startAmount: initial amount of the wallet
     * @param iconName: icon to use for the wallet
     * @param currency: currency of the wallet
     * @param creationDate: creation date of the wallet
     * @param lastAccess: last access of the wallet
     * @param budgetEnabled: flag on whether the wallet has a budgeting option enabled
     */
    data class UpdateWallet(
        val name: String? = null,
        val startAmount: Float? = null,
        val iconName: IconsMappedForDB? = null,
        val currency: Currency? = null,
        val creationDate: Date? = null,
        val lastAccess: Date? = null,
        val budgetEnabled: Boolean? = null,
    ): WalletEditEvent()
}