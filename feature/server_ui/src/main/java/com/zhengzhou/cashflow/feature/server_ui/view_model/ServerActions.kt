package com.zhengzhou.cashflow.feature.server_ui.view_model

internal sealed class ServerActions {
    data object Activate: ServerActions()
    data object Deactivate: ServerActions()
}