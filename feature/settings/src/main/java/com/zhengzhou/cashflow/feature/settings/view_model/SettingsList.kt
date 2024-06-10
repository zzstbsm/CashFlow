package com.zhengzhou.cashflow.feature.settings.view_model

import com.zhengzhou.cashflow.settings.R

internal enum class SettingsList(
    val title: Int,
    val description: Int?,
    val icon: Int?,
) {
    Backup(
        title = R.string.settings_option_title_backup,
        description = R.string.settings_option_description_backup,
        icon = R.drawable.ic_save,
    ),
    Restore(
        title = R.string.settings_option_title_restore,
        description = R.string.settings_option_description_restore,
        icon = null,
    );
    companion object {
        fun getAllSettings(): List<SettingsList> {
            return SettingsList.entries.toList()
        }
    }
}