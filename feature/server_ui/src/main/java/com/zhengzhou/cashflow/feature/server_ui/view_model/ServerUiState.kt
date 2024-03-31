package com.zhengzhou.cashflow.feature.server_ui.view_model

import com.zhengzhou.cashflow.core.server.api.ServerConfiguration

/**
 * @param serverRunningConfiguration stores the current running configuration. It is null if the server is not active
 */
internal data class ServerUiState(
    val serverRunningConfiguration: ServerConfiguration? = null,
)