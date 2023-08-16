package com.zhengzhou.cashflow.tools.calculator

fun mapCharToKeypadDigit(
    digit: Char
): KeypadDigit? {
    val keyList: List<KeypadDigit> = enumValues<KeypadDigit>().toList().filter { key ->
        !key.operation
    }
    if (digit == '.' || digit == ',') {
        return KeypadDigit.KeyDot
    }

    keyList.forEach { key ->
        if (key.value == digit.toString()) return key
    }

    return null
}