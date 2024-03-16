package com.zhengzhou.cashflow.tools.ui_elements.dropdown_menu

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager

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

    val focusManager = LocalFocusManager.current

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
            modifier = Modifier
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        onChangeExpanded(true)
                        focusManager.clearFocus()
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