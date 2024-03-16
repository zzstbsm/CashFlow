package com.zhengzhou.cashflow.total_balance.view_model

import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.total_balance.BalanceTabOptions
import com.zhengzhou.cashflow.total_balance.TimeFilterForSegmentedButton
import java.util.Date

sealed class BalanceEvent {
    /**
     * Choose which tab should be composed on the screen
     */
    data class SelectTabToShow(
        val selectedTab: BalanceTabOptions,
    ): BalanceEvent()

    /**
     * Set the time range for filtering the transactions to process
     * @param timeFilter groups the transactions based on the entries defined in [TimeFilterForSegmentedButton]
     * @param startDate set the start date of the filter
     * @param endDate set the end date of the filter
     * @param navigation true if the change of date is performed by the time period navigation buttons
     */
    data class SetTimeFilter(
        val timeFilter: TimeFilterForSegmentedButton?,
        val startDate: Date,
        val endDate: Date,
        val navigation: Boolean
    ): BalanceEvent()
    /**
     * Set the currency to show
     * @param currency selects the currency to show in the balance screen
     */
    data class SetWalletListByCurrency(val currency: Currency): BalanceEvent()
}