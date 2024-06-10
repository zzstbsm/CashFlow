package com.zhengzhou.cashflow.feature.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SettingsViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    private var writingUiState: Boolean = false

    init {


    }

    /**
     * Sets the selected option in settings.
     * @param settingsList is composed by [SettingsList] (that can be null) and
     * [ApplySettingBool] that is a boolean that eventually sets the field to null
     */
    private fun setUiState(
        settingsList: Pair<SettingsList?,ApplySettingBool> = Pair(null, false)
    ) {

        viewModelScope.launch {

            while (writingUiState) delay(5)
            writingUiState = true

            _uiState.value = uiState.value.copy(
                selectedOptionInSettings = settingsList.first ?: if (settingsList.second) {
                    null
                } else {
                    uiState.value.selectedOptionInSettings
                },
            )

            writingUiState = false
        }
    }
}