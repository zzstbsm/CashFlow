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

enum class KeypadDigit(
    val value: String,
    val operation: Boolean,
) {
    Key0(
        value = "0",
        operation = false,
    ),
    Key1(
        value = "1",
        operation = false,
    ),
    Key2(
        value = "2",
        operation = false,
    ),
    Key3(
        value = "3",
        operation = false,
    ),
    Key4(
        value = "4",
        operation = false,
    ),
    Key5(
        value = "5",
        operation = false,
    ),
    Key6(
        value = "6",
        operation = false,
    ),
    Key7(
        value = "7",
        operation = false,
    ),
    Key8(
        value = "8",
        operation = false,
    ),
    Key9(
        value = "9",
        operation = false,
    ),
    KeyDot(
        value = ".",
        operation = false,
    ),
    KeyEqual(
        value = "=",
        operation = true,
    ),
    KeyPlus(
        value = "+",
        operation = true,
    ),
    KeyMinus(
        value = "-",
        operation = true,
    ),
    KeyTimes(
        value = "\u00D7",
        operation = true,
    ),
    KeyDivide(
        value = "\u00F7",
        operation = true,
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