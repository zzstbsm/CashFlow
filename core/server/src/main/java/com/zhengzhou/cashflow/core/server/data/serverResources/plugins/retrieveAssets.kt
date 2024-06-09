package com.zhengzhou.cashflow.core.server.data.serverResources.plugins

import android.content.res.AssetManager
import android.util.Log

fun retrieveAsset(
    assetManager: AssetManager,
    position: String,
): ByteArray {
    val content: ByteArray = assetManager.open(position).use {
        it.readBytes()
    }
    Log.d("RetrieveAssets","Content: $position")
    Log.d("RetrieveAssets","Size: ${content.size}")
    return content
}