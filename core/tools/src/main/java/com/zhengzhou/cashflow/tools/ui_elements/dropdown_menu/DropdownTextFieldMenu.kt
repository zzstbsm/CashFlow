package com.zhengzhou.cashflow.tools.ui_elements.dropdown_menu

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun DropdownTextFieldMenu(
    label: String,
    value: String,
    enabled: Boolean = true,
    expanded: Boolean,
    onChangeExpanded: (Boolean) -> Unit,
    dropdownMenuContent: @Composable (ColumnScope.() -> Unit),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            label = {
                Text(text = label)
            },
            value = value,
            onValueChange = { },
            enabled = enabled,
            modifier = Modifier.onFocusEvent { focusState ->
                if (focusState.isFocused) {
                    onChangeExpanded(true)
                    keyboardController?.hide()
                }
            },
            maxLines = 1,
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onChangeExpanded(false)
            },
            content = dropdownMenuContent
        )
    }
}