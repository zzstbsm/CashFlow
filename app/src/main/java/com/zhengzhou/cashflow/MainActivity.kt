package com.zhengzhou.cashflow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Observer
import com.zhengzhou.cashflow.tools.ApplicationConfigurationService
import com.zhengzhou.cashflow.tools.EventMessages

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
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

// Allow to post Toast messages from ViewModels
open class Event<out T>(private val content: T) {
    private var hasBeenHandled = false
    //  Returns the content and prevents its use again.
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    // Returns the content, even if it's already been handled.
    fun peekContent(): T = content
}
