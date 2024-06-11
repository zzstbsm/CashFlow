package com.zhengzhou.cashflow.feature.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SettingsViewModel: ViewModel() {

    private var _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    private var writingUiState: Boolean = false

    init {


    }

    /**
     * Sets the selected option in settings.
     * @param loadTrigger: set on true to trigger the save
     * @param saveTrigger: set on true to trigger the save
     */
    private fun setUiState(
        loadTrigger: Boolean? = null,
        saveTrigger: Boolean? = null,
    ) {

        viewModelScope.launch {

            while (writingUiState) delay(5)
            writingUiState = true

            _uiState.value = uiState.value.copy(
                loadTrigger = loadTrigger ?: uiState.value.loadTrigger,
                saveTrigger = saveTrigger ?: uiState.value.saveTrigger,
            )

            writingUiState = false
        }
    }

    fun onEvent(
        settingsEvents: SettingsEvents,
    ) {
        when (settingsEvents) {
            is SettingsEvents.ActivateTriggerLoad -> {
                setUiState(
                    loadTrigger = true,
                )
            }
            is SettingsEvents.ActivateTriggerSave -> {
                setUiState(
                    saveTrigger = true
                )
            }
        }
    }

    fun accessLoadTrigger(): Boolean {
        return if (uiState.value.loadTrigger) {
            setUiState(
                loadTrigger = false,
            )
            true
        } else false
    }

    fun accessSaveTrigger(): Boolean {
        return if (uiState.value.saveTrigger) {
            setUiState(
                saveTrigger = false,
            )
            true
        } else false
    }

}