package com.zhengzhou.cashflow.tools.accessToSharedStorage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoadJsonWithSAF(
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
) {

    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/json"
    }

    // Trigger the SAF intent
    launcher.launch(intent)
}

@Composable
fun handleLoadJsonResult(
    context: Context,
    coroutineScope: CoroutineScope,
    onJsonLoaded: (String) -> Unit,
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let {
                coroutineScope.launch {
                    val jsonData = loadJsonFromFile(context, it)
                    onJsonLoaded(jsonData)
                }
            }
        }
    }
}

private fun loadJsonFromFile(context: Context, uri: Uri): String {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.bufferedReader().use { reader ->
                reader.readText()
            }
        } ?: ""
    } catch (e: Exception) {
        // Handle exceptions, e.g., log the error or show a message to the user
        e.printStackTrace()
        ""
    }
}