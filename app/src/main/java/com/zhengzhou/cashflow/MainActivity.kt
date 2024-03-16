package com.zhengzhou.cashflow

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.zhengzhou.cashflow.core.tools.PreloadTransactions
import com.zhengzhou.cashflow.database.LoadDefaultCategories
import com.zhengzhou.cashflow.database.api.DatabaseInstance
import com.zhengzhou.cashflow.database.api.use_case.categoryUseCases.implementations.CategoryUseCases
import com.zhengzhou.cashflow.navigation.NavigationApp
import com.zhengzhou.cashflow.tools.EventMessages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            val repository = DatabaseInstance.getRepository()
            val categoryUseCases = CategoryUseCases(repository)
            val coroutineScope = CoroutineScope(Dispatchers.Default)
            coroutineScope.launch {
                categoryUseCases.getCategoryList().collect {
                    if (it.isEmpty())
                        LoadDefaultCategories.configureTableCategory()

                }
            }

            PreloadTransactions.load()

            MaterialTheme {
                NavigationApp(repository)
            }

            EventMessages.messageId.observe(this) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
            EventMessages.message.observe(this) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}

