package com.zhengzhou.cashflow.tools.calculator

internal data class CalculatorState(
    var amountInMemory: String = "0",
    var amountOnScreen: String = "0",
    var currentOperation: String = Operations.No.op,
    var numberOfDecimals: Int = 0,
    var isDecimal: Boolean = false,
    var canOverwrite: Boolean = false
)