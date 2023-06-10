package com.zhengzhou.cashflow.data

import androidx.annotation.StringRes
import com.zhengzhou.cashflow.R
import java.text.NumberFormat
import java.util.*

enum class Currency(
    val locale: Locale,
    val abbreviation: String,
    @StringRes val nameCurrency: Int
) {
    USA(
        locale = Locale.US,
        abbreviation = "USD",
        nameCurrency = R.string.currency_usd_name
    ),

    EUR(
        locale = Locale.ITALY,
        abbreviation = "EUR",
        nameCurrency = R.string.currency_usd_name
    ),

    SEK(
        locale = Locale("sv","se"),
        abbreviation = "SEK",
        nameCurrency = R.string.currency_sek_name
    )
}

fun setCurrency(
    abbreviation: String
) : Currency? {

    // Check if the currency is supported
    // Return currency if supported
    Currency.values().forEach { currency: Currency ->
        if (currency.abbreviation == abbreviation) {
            return currency
        }
    }

    // The currency is not supported
    return null

}

fun formatCurrency(
    currency : NumberFormat,
    amount : Float,
) : String {
    return currency.format(amount)
}

fun setCurrencyFormatter(currencyString: String) : NumberFormat {

    val currency: Currency? = setCurrency(currencyString)

    return if (currency != null) {
        NumberFormat.getCurrencyInstance(currency.locale)
    } else {
        NumberFormat.getCurrencyInstance()
    }
}