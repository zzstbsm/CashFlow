package com.zhengzhou.cashflow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import com.zhengzhou.cashflow.navigation.NavigationApp
import com.zhengzhou.cashflow.tools.ApplicationConfigurationService
import com.zhengzhou.cashflow.tools.EventMessages
import com.zhengzhou.cashflow.tools.LoadDefaultCategories
import com.zhengzhou.cashflow.tools.PreloadTransactions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{

            val repository = com.zhengzhou.cashflow.database.DatabaseRepository.get()
            val coroutineScope = CoroutineScope(Dispatchers.Default)
            coroutineScope.launch {
                repository.getCategoryList().collect {
                    if (it.isEmpty())
                        LoadDefaultCategories.configureTableCategory()

                }
            }

            PreloadTransactions.load()

            MaterialTheme {
                NavigationApp()
            }

            val context = LocalContext.current
            context.startService(Intent(context,ApplicationConfigurationService::class.java))

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

