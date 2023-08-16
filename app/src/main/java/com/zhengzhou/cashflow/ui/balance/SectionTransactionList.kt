package com.zhengzhou.cashflow.ui.balance

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.customUiElements.CategoryIcon
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Transaction
import java.text.NumberFormat

@Composable
fun TransactionEntry(
    transaction: Transaction,
    category: Category,
    currencyFormatter : NumberFormat,
    onClickTransaction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = modifier
            .height(60.dp)
            .clickable { onClickTransaction() },
        shape = MaterialTheme.shapes.large,
    ) {

        val firstLineStyle = MaterialTheme.typography.bodyLarge
        val secondLineStyle = MaterialTheme.typography.bodySmall

        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CategoryIcon(
                iconName = category.iconName,
                contentDescription = null, //TODO add description
                modifier = Modifier
                    .size(54.dp)
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = category.name,
                        style = firstLineStyle,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = currencyFormatter.format(transaction.amount),
                        style = firstLineStyle,
                        textAlign = TextAlign.End,
                        color = if (transaction.amount >= 0) {
                            Color.Green
                        } else {
                            Color.Red
                        }
                    )
                }
                Row {
                    Text(
                        text = DateFormat.format(
                            "MMM dd",
                            transaction.date
                        ).toString(),
                        style = secondLineStyle
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "-",
                        style = secondLineStyle
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = transaction.description,
                        style = secondLineStyle
                    )
                }
            }
        }
    }
}