package com.zhengzhou.cashflow.tools.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorTest {

    @Test
    fun testComputeWithNoCancellationsNoInitialization() {
        val sequence = "1+1.2+4.06-1.0*2/5="
        val expectedResultString = "2.1"

        val calculator = Calculator()

        sequence.forEach { digitChar ->
            val key = mapCharToKeypadDigitWithOperations(digitChar)
            calculator.addKey(key!!)
        }
        assertEquals(expectedResultString,calculator.onScreenString())
    }

    @Test
    fun testComputeTestWithCancellationsNoInitialization() {

        val sequence = "1+1.2+4.06-1.0*2/55§="
        val expectedResultString = "2.1"

        val calculator = Calculator()
        sequence.forEach { digitChar ->
            val key = mapCharToKeypadDigitWithOperations(digitChar)
                if (key == null) calculator.dropLastDigit() else calculator.addKey(key)
        }
        assertEquals(expectedResultString,calculator.onScreenString())
    }
}