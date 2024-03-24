package com.zhengzhou.cashflow.tools

import com.zhengzhou.cashflow.data.Currency
import java.text.NumberFormat

fun removeSpaceFromStringEnd(text: String): String {

    var value = text
    while (value.isNotEmpty() && value.last() == ' ') {
        value = value.dropLast(1)
    }
    return value
}

class CurrencyFormatter private constructor() {

    companion object {

        private var currentCurrency: Currency? = null
        private var currencyNumberFormat: NumberFormat? = null

        fun formatCurrency(
            currency: Currency,
            amount: Float,
        ) : String {

             if (currentCurrency != currency || currencyNumberFormat == null) {
                 currentCurrency = currency
                 currencyNumberFormat = NumberFormat.getCurrencyInstance(currentCurrency!!.locale)
             }

            return currencyNumberFormat!!.format(amount)
        }
    }

}