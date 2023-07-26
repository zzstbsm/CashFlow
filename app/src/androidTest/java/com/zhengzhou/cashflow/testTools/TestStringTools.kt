package com.zhengzhou.cashflow.testTools

import com.zhengzhou.cashflow.tools.removeEndSpaces
import org.junit.Assert.*

import org.junit.Test

class TestStringTools {

    @Test
    fun testRemoveEndSpaces() {

        val text1 = "Ciao "
        val text2 = "Ciao  "
        val text3 = " Ciao"
        val text4 = "Ciao"
        val text5 = " Ciao   "

        assertEquals("Ciao",removeEndSpaces(text1))
        assertEquals("Ciao",removeEndSpaces(text2))
        assertEquals(" Ciao",removeEndSpaces(text3))
        assertEquals("Ciao",removeEndSpaces(text4))
        assertEquals(" Ciao",removeEndSpaces(text5))

    }
}