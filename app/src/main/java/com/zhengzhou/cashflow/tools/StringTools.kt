package com.zhengzhou.cashflow.tools

fun removeEndSpaces(text: String): String {
    var name = text
    while (name.isNotEmpty() && name.last() == ' ') {
        name = name.dropLast(1)
    }
    return name
}