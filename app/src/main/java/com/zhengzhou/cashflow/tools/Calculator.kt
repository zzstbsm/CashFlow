package com.zhengzhou.cashflow.tools

import com.zhengzhou.cashflow.R
import kotlin.math.roundToInt

class Calculator {

    private enum class Operations(
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

    private data class CalculatorState(
        var amountInMemory: String = "0",
        var amountOnScreen: String = "0",
        var currentOperation: String = Operations.No.op,
        var numberOfDecimals: Int = 0,
        var isDecimal: Boolean = false,
        var canOverwrite: Boolean = false
    )

    private var state = CalculatorState()

    private fun calculatorEqual() {

        // If canOverwrite is active and there is an operation selected, it was just an error of the user
        if (state.canOverwrite) return

        // Do something only if there is a selected operation
        if (state.currentOperation != Operations.No.op) {
            val result: Float = when (state.currentOperation) {
                Operations.Sum.op -> {
                    state.amountInMemory.toFloat() + state.amountOnScreen.toFloat()
                }
                Operations.Subtraction.op -> {
                    state.amountInMemory.toFloat() - state.amountOnScreen.toFloat()
                }
                Operations.Multiplication.op -> {
                    state.amountInMemory.toFloat() * state.amountOnScreen.toFloat()
                }
                Operations.Division.op -> {
                    // Divide only if it is not by zero
                    if (state.amountOnScreen.toFloat() == 0f) {
                        EventMessages.sendMessageId(R.string.err_divide_by_zero) // TODO: Insert in string.xml
                        state.amountOnScreen.toFloat()
                    } else {
                        state.amountInMemory.toFloat() / state.amountOnScreen.toFloat()
                    }
                }
                else -> state.amountOnScreen.toFloat()
            }
            state.amountOnScreen = ((result * 100).roundToInt() / 100f).toString()

            state.amountInMemory = state.amountOnScreen
            state.canOverwrite = true

            state.amountInMemory = "0"

            state.currentOperation = Operations.No.op

            // Count the decimals in the result
            var onScreen = state.amountOnScreen
            var nDecimals = 0
            var haveDecimals = false

            // Run until there are no digits remaining or we have found the dot
            while (!haveDecimals) {
                // No digits remaining
                if (onScreen == "") break
                else if (onScreen.last() == '.') {
                    haveDecimals = true
                } else {
                    onScreen = onScreen.dropLast(1)
                    nDecimals++
                }
            }

            if (!haveDecimals) {
                nDecimals = 0
            }
            state.numberOfDecimals = nDecimals
            state.isDecimal = haveDecimals

        }
    }

    fun addKey(key: KeypadDigit) {
        // If the digit is an operation
        if (key.operation) {
            // If the operation is equal
            if (key == KeypadDigit.KeyEqual) {
                calculatorEqual()
            }
            // The operation is different
            else {

                if (state.currentOperation != Operations.No.op) {
                    calculatorEqual()
                }

                state.currentOperation = key.value
                // Copy the amount in memory and allow to set the amount on screen to zero
                // if number is pressed
                state.amountInMemory = state.amountOnScreen
                state.canOverwrite = true
                state.isDecimal = false
                state.numberOfDecimals = 0
            }
        }
        // If the digit is a dot
        else {
            if (key == KeypadDigit.KeyDot) {
                // Do something if the decimal has not been yet pressed
                if (!state.isDecimal) {
                    if (state.canOverwrite) {
                        state.amountOnScreen = "0"
                    }
                    // TODO: there is a bug with the dot
                    state.isDecimal = true
                    state.amountOnScreen += key.value
                } else addDigit("")
            }
            // If the digits are numbers
            else {
                // Do something only if there re less than 2 decimals
                if (state.numberOfDecimals < 2) {
                    // If the dot has been pressed
                    if (state.isDecimal) {
                        state.numberOfDecimals++
                    }
                    addDigit(key.value)
                } else addDigit("")
            }
        }
    }

    fun dropLastDigit() {
        // Remove data in memory
        state.canOverwrite = false

        // Remove last character
        if (state.amountOnScreen != "0") {
            if (state.numberOfDecimals > 0) {
                state.numberOfDecimals--
            }
            else if (state.amountOnScreen.last() == '.') {
                state.isDecimal = false
            }
            state.amountOnScreen = state.amountOnScreen.dropLast(1)
        }
        // If now the string is empty
        if (state.amountOnScreen == "") {
            state.amountOnScreen = "0"
            state.amountInMemory = "0"
        }
    }

    private fun addDigit(digit: String) {
        if (state.canOverwrite) {
            state.amountOnScreen = digit
            state.canOverwrite = false
        } else {
            if (state.amountOnScreen == "0") {
                state.amountOnScreen = digit
            } else {
                state.amountOnScreen += digit
            }
        }
    }

    fun onScreenString(): String = state.amountOnScreen
}