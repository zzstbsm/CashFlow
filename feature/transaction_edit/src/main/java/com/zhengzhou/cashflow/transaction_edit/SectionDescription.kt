package com.zhengzhou.cashflow.transaction_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
internal fun DescriptionSection(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val keyboardFocus = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = text,
            label = {
                Text(text = stringResource(id = R.string.description_transaction))
            },
            maxLines = 1,
            onValueChange = {
                onTextChange(it)
            },
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(.8f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardFocus.clearFocus()
                }
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_description),
            contentDescription = null,
            modifier = modifier.size(32.dp)
        )
    }
}