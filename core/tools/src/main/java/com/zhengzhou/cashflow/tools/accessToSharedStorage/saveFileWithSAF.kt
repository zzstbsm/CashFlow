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

fun saveJsonWithSAF(
    fileName: String,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
) {

    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/json"
        putExtra(Intent.EXTRA_TITLE, fileName)
    }

    // Trigger the SAF intent
    launcher.launch(intent)
}

@Composable
fun handleSaveJsonResult(
    context: Context,
    coroutineScope: CoroutineScope,
    jsonData: String,
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let {
                coroutineScope.launch {
                    saveJsonToFile(context, it, jsonData)
                }
            }
        }
    }
}

private fun saveJsonToFile(context: Context, uri: Uri, jsonData: String) {
    try {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(jsonData.toByteArray())
        }
    } catch (e: Exception) {
        // Handle exceptions, e.g., log the error or show a message to the user
        e.printStackTrace()
    }
}