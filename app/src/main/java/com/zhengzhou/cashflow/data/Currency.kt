package com.zhengzhou.cashflow.data

import androidx.annotation.StringRes
import com.zhengzhou.cashflow.R
import java.text.NumberFormat
import java.util.*

enum class Currency(
    val locale: Locale,
    val abbreviation: String,
    @StringRes val nameCurrency: Int,
    val iconEmojiUnicode: String,
) {

    EUR(
        locale = Locale.ITALY,
        abbreviation = "EUR",
        nameCurrency = R.string.currency_usd_name,
        iconEmojiUnicode = "\uD83C\uDDEA\uD83C\uDDFA"
    );
    /*
    USA(
        locale = Locale.US,
        abbreviation = "USD",
        nameCurrency = R.string.currency_usd_name,
        iconEmojiUnicode = "\uD83C\uDDFA\uD83C\uDDF8",
    ),

    SEK(
        locale = Locale("sv","se"),
        abbreviation = "SEK",
        nameCurrency = R.string.currency_sek_name,
        iconEmojiUnicode = "\uD83C\uDDF8\uD83C\uDDEA"
    );
     */

    companion object {
        fun supportedCurrencyList() : List<Currency> {
            return Currency.values().toList().sortedBy { currency ->
                currency.abbreviation
            }
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

    }
}