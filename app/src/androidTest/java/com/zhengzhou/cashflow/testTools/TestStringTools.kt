package com.zhengzhou.cashflow.testTools

import com.zhengzhou.cashflow.tools.removeSpaceFromStringEnd
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

        assertEquals("Ciao",removeSpaceFromStringEnd(text1))
        assertEquals("Ciao",removeSpaceFromStringEnd(text2))
        assertEquals(" Ciao",removeSpaceFromStringEnd(text3))
        assertEquals("Ciao",removeSpaceFromStringEnd(text4))
        assertEquals(" Ciao",removeSpaceFromStringEnd(text5))

    }
}