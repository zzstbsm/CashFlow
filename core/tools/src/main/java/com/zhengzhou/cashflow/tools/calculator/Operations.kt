package com.zhengzhou.cashflow.tools.calculator

internal enum class Operations(
    val op: String
) {
    No(
        op = ""
    ),
    Sum(
        op = "+"
    ),
    Subtraction(
        op = "-"
    ),
    Multiplication(
        op = "\u00D7"
    ),
    Division(
        op = "\u00F7"
    ),
}