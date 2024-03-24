package com.zhengzhou.cashflow.feature.server_ui.view_model

import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.core.server.api.ServerEngineInterface
import com.zhengzhou.cashflow.core.server.api.ServerInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ServerUiViewModel(
    val connectivityManager: ConnectivityManager
): ViewModel() {

    private var _uiState = MutableStateFlow(ServerUiState())
    val uiState: StateFlow<ServerUiState> = _uiState.asStateFlow()

    private var server: ServerEngineInterface

    private var isWriting = false

    init {
        ServerInstance.initialize()
        server = ServerInstance.getServer()!!
    }

    /**
     * @param serverActive set the server state
     * @param ipAddress takes a pair: [ipAddress].first is the string to write, [ipAddress].second verify if the string has to be written.
     */
    private fun setUiState(
        serverActive: Boolean? = null,

        ipAddress: Pair<String?,Boolean> = Pair(null,false),
    ) {
        viewModelScope.launch {
            while (isWriting) delay(5)

            isWriting = true
            val writeIpAddress = if (ipAddress.second) ipAddress.first else uiState.value.ipAddress

            _uiState.value = uiState.value.copy(
                serverActive = serverActive ?: uiState.value.serverActive,
                ipAddress = writeIpAddress
            )

            isWriting = false

        }
    }

    fun onEvent(event: ServerEvent) {
        when (event) {
            is ServerEvent.SetServerActivationState -> {
                viewModelScope.launch {
                    when (event.serverState) {
                        ServerActions.Activate -> {

                            server.start()
                            val ipAddress = server.getLocalIP()

                            setUiState(
                                serverActive = true,
                                ipAddress = Pair(ipAddress,true),
                            )
                        }
                        ServerActions.Deactivate -> {
                            server.stop()
                            setUiState(
                                serverActive = false,
                                ipAddress = Pair(null,true),
                            )
                        }
                    }
                }
            }
        }
    }

}