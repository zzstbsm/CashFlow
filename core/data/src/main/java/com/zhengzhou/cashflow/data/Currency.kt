package com.zhengzhou.cashflow.data

import androidx.annotation.StringRes
import java.util.Locale

enum class Currency(
    val locale: Locale,
    @StringRes val nameCurrency: Int,
    val iconEmojiUnicode: String,
) {

    AUD(
        locale = Locale("en","au"),
        nameCurrency = R.string.currency_aud_name,
        iconEmojiUnicode = "\uD83C\uDDE6\uD83C\uDDFA"
    ),
    CAD(
        locale = Locale("en","ca"),
        nameCurrency = R.string.currency_cad_name,
        iconEmojiUnicode = "\uD83C\uDDE8\uD83C\uDDE6"
    ),
    GBP(
        locale = Locale.UK,
        nameCurrency = R.string.currency_gbp_name,
        iconEmojiUnicode = "\uD83C\uDDEC\uD83C\uDDE7"
    ),
    EUR(
        locale = Locale.ITALY,
        nameCurrency = R.string.currency_eur_name,
        iconEmojiUnicode = "\uD83C\uDDEA\uD83C\uDDFA"
    ),
    INR(
        locale = Locale("hi","in"),
        nameCurrency = R.string.currency_inr_name,
        iconEmojiUnicode = "\uD83C\uDDEE\uD83C\uDDF3",
    ),
    JPY(
        locale = Locale.JAPAN,
        nameCurrency = R.string.currency_jpy_name,
        iconEmojiUnicode = "\uD83C\uDDEF\uD83C\uDDF5",
    ),
    PLN(
        locale = Locale("pl","pl"),
        nameCurrency = R.string.currency_pln_name,
        iconEmojiUnicode = "\uD83C\uDDF5\uD83C\uDDF1",
    ),
    RUB(
        locale = Locale("ru","ru"),
        nameCurrency = R.string.currency_rub_name,
        iconEmojiUnicode = "\uD83C\uDDF7\uD83C\uDDFA",
    ),
    USD(
        locale = Locale.US,
        nameCurrency = R.string.currency_usd_name,
        iconEmojiUnicode = "\uD83C\uDDFA\uD83C\uDDF8",
    ),
    SEK(
        locale = Locale("sv","se"),
        nameCurrency = R.string.currency_sek_name,
        iconEmojiUnicode = "\uD83C\uDDF8\uD83C\uDDEA"
    );

    companion object {
        fun supportedCurrencyList() : List<Currency> {
            return Currency.values().toList().sortedBy { currency ->
                currency.name
            }
        }

        fun getDefaultCurrency(): Currency {
            return EUR
        }

        fun setCurrency(
            name: String
        ) : Currency? {

            // Check if the currency is supported
            // Return currency if supported
            Currency.values().forEach { currency: Currency ->
                if (currency.name == name) {
                    return currency
                }
            }

            // The currency is not supported
            return null

        }
    }
}