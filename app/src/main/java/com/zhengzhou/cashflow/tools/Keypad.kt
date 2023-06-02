package com.zhengzhou.cashflow.tools

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

sealed class KeypadDigit(
    val value: String,
    val operation: Boolean
) {
    object Key0: KeypadDigit(
        value = "0",
        operation = false
    )
    object Key1: KeypadDigit(
        value = "1",
        operation = false
    )
    object Key2: KeypadDigit(
        value = "2",
        operation = false
    )
    object Key3: KeypadDigit(
        value = "3",
        operation = false
    )
    object Key4: KeypadDigit(
        value = "4",
        operation = false
    )
    object Key5: KeypadDigit(
        value = "5",
        operation = false
    )
    object Key6: KeypadDigit(
        value = "6",
        operation = false
    )
    object Key7: KeypadDigit(
        value = "7",
        operation = false
    )
    object Key8: KeypadDigit(
        value = "8",
        operation = false
    )
    object Key9: KeypadDigit(
        value = "9",
        operation = false
    )
    object KeyDot: KeypadDigit(
        value = ".",
        operation = false
    )
    object KeyEqual: KeypadDigit(
        value = "=",
        operation = true
    )
    object KeyPlus: KeypadDigit(
        value = "+",
        operation = true
    )
    object KeyMinus: KeypadDigit(
        value = "-",
        operation = true
    )
    object KeyTimes: KeypadDigit(
        value = "\u00D7",
        operation = true
    )
    object KeyDivide: KeypadDigit(
        value = "\u00F7",
        operation = true
    )
}

@Preview
@Composable
private fun CircularDigitPreview() {
    KeypadDigitButton(
        key = KeypadDigit.KeyTimes,
        onClick = {  }
    )
}

@Composable
fun KeypadDigitButton(
    key: KeypadDigit,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    shape: Shape = CircleShape,
    onClick: (KeypadDigit) -> Unit
) {
        ElevatedButton(
            onClick = { onClick(key) },
            modifier = modifier,
            shape = shape,
        ) {
            Box {
                Text(
                    text = key.value,
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
}