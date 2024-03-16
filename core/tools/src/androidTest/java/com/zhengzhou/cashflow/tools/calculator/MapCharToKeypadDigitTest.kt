package com.zhengzhou.cashflow.tools.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class MapCharToKeypadDigitTest {

    private val invalidKeys = "qwertyuiopasdfghjklzxcvbnm!\"|£$%&()?^èòàé°ç"

    @Test
    fun mapCharToKeypadDigitTest() {

        val keys: List<KeypadDigit> = KeypadDigit.values().toList().filter { key ->
            !key.operation
        }

        keys.forEach { key ->
            assertEquals(key, mapCharToKeypadDigit(key.value.first()))
        }

        invalidKeys.forEach {
            assertEquals(null, mapCharToKeypadDigit(it))
        }

        assertEquals(KeypadDigit.KeyDot, mapCharToKeypadDigit('.'))
        assertEquals(KeypadDigit.KeyDot, mapCharToKeypadDigit(','))

    }

    @Test
    fun mapCharToKeypadDigitWithOperations() {

        val keys: List<KeypadDigit> = KeypadDigit.values().toList()

        keys.forEach { key ->
            assertEquals(key, mapCharToKeypadDigitWithOperations(key.value.first()))
        }

        invalidKeys.forEach {
            assertEquals(null, mapCharToKeypadDigitWithOperations(it))
        }

        assertEquals(KeypadDigit.KeyDot, mapCharToKeypadDigitWithOperations('.'))
        assertEquals(KeypadDigit.KeyDot, mapCharToKeypadDigitWithOperations(','))
        assertEquals(KeypadDigit.KeyTimes, mapCharToKeypadDigitWithOperations('*'))
        assertEquals(KeypadDigit.KeyDivide, mapCharToKeypadDigitWithOperations('/'))
    }
}