package com.zhengzhou.cashflow.tools

fun removeSpaceFromStringEnd(text: String): String {

    var value = text
    while (value.isNotEmpty() && value.last() == ' ') {
        value = value.dropLast(1)
    }
    return value
}