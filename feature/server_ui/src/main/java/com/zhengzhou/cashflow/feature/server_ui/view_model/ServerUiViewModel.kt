package com.zhengzhou.cashflow.feature.server_ui.view_model

import android.content.res.AssetManager
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhengzhou.cashflow.core.server.api.ServerConfiguration
import com.zhengzhou.cashflow.core.server.api.ServerEngineInterface
import com.zhengzhou.cashflow.core.server.api.ServerInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val DEFAULT_PORT = 8080

internal class ServerUiViewModel(
    val connectivityManager: ConnectivityManager,
    private val assetManager: AssetManager,
): ViewModel() {

    private var _uiState = MutableStateFlow(ServerUiState())
    val uiState: StateFlow<ServerUiState> = _uiState.asStateFlow()

    private var _server: ServerEngineInterface

    private var _isWriting = false

    init {
        ServerInstance.initialize()
        _server = ServerInstance.getServer()!!

        // Check if the server is active
        setUiState(
            Pair(_server.getHostConfiguration(connectivityManager),true)
        )

    }

    /**
     * @param serverRunningConfiguration takes a pair: [serverRunningConfiguration].first is the string to write, [serverRunningConfiguration].second verify if the string has to be written.
     */
    private fun setUiState(
        serverRunningConfiguration: Pair<ServerConfiguration?,Boolean> = Pair(null,false),
    ) {
        viewModelScope.launch {

            while (_isWriting) delay(5)

            _isWriting = true
            val writeServerRunningConfiguration = if (serverRunningConfiguration.second) serverRunningConfiguration.first else uiState.value.serverRunningConfiguration

            _uiState.value = uiState.value.copy(
                serverRunningConfiguration = writeServerRunningConfiguration
            )

            _isWriting = false

        }
    }

    fun onEvent(event: ServerEvent) {
        when (event) {
            is ServerEvent.SetServerActivationState -> {
                viewModelScope.launch {
                    when (event.serverState) {
                        ServerActions.Activate -> {

                            _server.start(
                                port = DEFAULT_PORT,
                                assetManager = assetManager
                            )
                            val serverRunningConfiguration = _server.getHostConfiguration(connectivityManager = connectivityManager)

                            setUiState(
                                serverRunningConfiguration = Pair(serverRunningConfiguration,true),
                            )
                        }
                        ServerActions.Deactivate -> {
                            _server.stop()
                            setUiState(
                                serverRunningConfiguration = Pair(null,true),
                            )
                        }
                    }
                }
            }
        }
    }

}