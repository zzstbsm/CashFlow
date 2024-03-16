package com.zhengzhou.cashflow.tools.ui_elements.transaction

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.tools.CurrencyFormatter
import com.zhengzhou.cashflow.tools.ui_elements.category.CategoryIcon

@Composable
fun SectionTransactionEntry(
    transaction: Transaction,
    category: Category,
    currency: Currency,
    onClickTransaction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .height(60.dp)
            .clickable { onClickTransaction() },
        // shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.large,
    ) {
        val firstLineStyle = MaterialTheme.typography.bodyLarge
        val secondLineStyle = MaterialTheme.typography.bodySmall

        Row(
            modifier = modifier
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CategoryIcon(
                iconName = category.iconName,
                contentDescription = category.name,
                modifier = modifier
                    .size(54.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.name,
                        style = firstLineStyle,
                        textAlign = TextAlign.Start,
                        modifier = modifier.weight(1f)
                    )
                    Text(
                        text = CurrencyFormatter.formatCurrency(currency, transaction.amount),
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
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = "-",
                        style = secondLineStyle
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = transaction.description,
                        style = secondLineStyle,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}