package com.zhengzhou.cashflow.themes

import org.junit.Assert.assertEquals
import org.junit.Test

class IconsMappedForDBTest {

    @Test
    fun getResourceTest() {

        val iconsList = IconsMappedForDB.values().toList()

        iconsList.forEach {
            val icon = IconsMappedForDB.setIcon(it.name)
            assertEquals(it,icon)
        }

        val nonValid = listOf(
            "Cheese","Chicken","Kitchen","Idk, maybe it is broken"
        )
        nonValid.forEach {
            assertEquals(null,IconsMappedForDB.setIcon(it))
        }


    }
}