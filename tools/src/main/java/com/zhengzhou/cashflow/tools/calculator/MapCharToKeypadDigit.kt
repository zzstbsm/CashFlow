package com.zhengzhou.cashflow.tools.calculator

fun mapCharToKeypadDigit(
    digit: Char
): KeypadDigit? {
    val keyList: List<KeypadDigit> = KeypadDigit.values().toList().filter { key ->
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
fun mapCharToKeypadDigitWithOperations(
    digit: Char
): KeypadDigit? {
    val keyList: List<KeypadDigit> = KeypadDigit.values().toList()

    when (digit) {
        '.' -> return KeypadDigit.KeyDot
        ',' -> return KeypadDigit.KeyDot
        '*' -> return KeypadDigit.KeyTimes
        '/' -> return KeypadDigit.KeyDivide
    }

    keyList.forEach { key ->
        if (key.value == digit.toString()) return key
    }

    return null
}