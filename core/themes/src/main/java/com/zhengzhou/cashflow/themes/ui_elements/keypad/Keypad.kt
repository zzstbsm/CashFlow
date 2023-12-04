package com.zhengzhou.cashflow.themes.ui_elements.keypad

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
import com.zhengzhou.cashflow.tools.calculator.KeypadDigit

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