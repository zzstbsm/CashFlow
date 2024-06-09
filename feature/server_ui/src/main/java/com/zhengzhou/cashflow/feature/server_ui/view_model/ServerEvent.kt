package com.zhengzhou.cashflow.feature.server_ui.view_model

internal sealed class ServerEvent {

    data class SetServerActivationState(val serverState: ServerActions): ServerEvent()

}
