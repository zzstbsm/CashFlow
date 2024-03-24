package com.zhengzhou.cashflow.tools.calculator

enum class KeypadDigit(
    val value: String,
    val operation: Boolean,
) {
    Key0(
        value = "0",
        operation = false,
    ),
    Key1(
        value = "1",
        operation = false,
    ),
    Key2(
        value = "2",
        operation = false,
    ),
    Key3(
        value = "3",
        operation = false,
    ),
    Key4(
        value = "4",
        operation = false,
    ),
    Key5(
        value = "5",
        operation = false,
    ),
    Key6(
        value = "6",
        operation = false,
    ),
    Key7(
        value = "7",
        operation = false,
    ),
    Key8(
        value = "8",
        operation = false,
    ),
    Key9(
        value = "9",
        operation = false,
    ),
    KeyDot(
        value = ".",
        operation = false,
    ),
    KeyEqual(
        value = "=",
        operation = true,
    ),
    KeyPlus(
        value = "+",
        operation = true,
    ),
    KeyMinus(
        value = "-",
        operation = true,
    ),
    KeyTimes(
        value = "\u00D7",
        operation = true,
    ),
    KeyDivide(
        value = "\u00F7",
        operation = true,
    ),
    KeyBack(
        value = "\u232B",
        operation = false
    ),
}