package com.zhengzhou.cashflow.feature.settings.view_model

internal sealed class SettingsEvents {
    data object ActivateTriggerSave: SettingsEvents()
    data object ActivateTriggerLoad: SettingsEvents()
}