package com.zhengzhou.cashflow.testViewModel

import android.annotation.SuppressLint
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.tools.ConfigurationFirstStartup
import com.zhengzhou.cashflow.ui.manageCategories.ManageCategoriesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestManageCategories() {

    private lateinit var jobConfigureCategories: Job

    @Before
    fun initializeData() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        jobConfigureCategories = coroutineScope.launch {
            ConfigurationFirstStartup.configureTableCategory()
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Test
    @Throws(Exception::class)
    fun initViewModel() {

        val coroutineScope = CoroutineScope(Dispatchers.Default)

        val manageCategoriesViewModel = ManageCategoriesViewModel()
        val manageCategoriesUiState = manageCategoriesViewModel.uiState.value

        coroutineScope.launch {

            if (manageCategoriesUiState.isLoading) {
                delay(5)
            }

            jobConfigureCategories.join()
            val expenseCategories = ConfigurationFirstStartup.setDefaultExpenseCategories()
            val depositCategories = ConfigurationFirstStartup.setDefaultDepositCategories()

            expenseCategories.forEach { category: Category ->
                assertTrue(category in manageCategoriesUiState.listCategories)
            }
            depositCategories.forEach { category: Category ->
                assertTrue(category in manageCategoriesUiState.listCategories)
            }
        }

    }
}
