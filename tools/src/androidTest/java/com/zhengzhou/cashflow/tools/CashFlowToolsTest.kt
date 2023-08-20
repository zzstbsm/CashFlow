package com.zhengzhou.cashflow.tools

import com.zhengzhou.cashflow.data.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test

class CashFlowToolsTest {
    private val transactionList = listOf(
        Transaction.newEmpty().copy(
            amount = 50f
        ),
        Transaction.newEmpty().copy(
            amount = 20f
        ),
        Transaction.newEmpty().copy(
            amount = -30f
        ),
        Transaction.newEmpty().copy(
            amount = -25.4f
        ),
        Transaction.newEmpty().copy(
            amount = 14.5f
        ),
        Transaction.newEmpty().copy(
            amount = -23.4f
        ),
        Transaction.newEmpty().copy(
            amount = -87.5f
        ),
    )

    @Test
    fun testBalanceFlowIn() {
        assertEquals(84.5f,CashFlowTools.balanceFlowIn(transactionList))
    }
    @Test
    fun testBalanceFlowOut() {
        assertEquals(-166.3f,CashFlowTools.balanceFlowOut(transactionList))
    }
}