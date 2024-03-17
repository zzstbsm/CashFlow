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
import androidx.compose.foundation.layout.requiredSize
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
            .height(64.dp)
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable { onClickTransaction() },
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.large,
    ) {
        val firstLineStyle = MaterialTheme.typography.bodyLarge
        val secondLineStyle = MaterialTheme.typography.bodySmall

        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            CategoryIcon(
                iconName = category.iconName,
                contentDescription = category.name,
                modifier = Modifier
                    .requiredSize(48.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = category.name,
                        style = firstLineStyle,
                        textAlign = TextAlign.Start,
                        //modifier = modifier.weight(1f)
                    )
                    Text(
                        text = CurrencyFormatter.formatCurrency(currency, transaction.amount),
                        style = firstLineStyle,
                        textAlign = TextAlign.End,
                        color = if (transaction.amount >= 0) {
                            Color.Green
                        } else {
                            Color.Red
                        },
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = DateFormat.format(
                            "MMM dd",
                            transaction.date
                        ).toString(),
                        style = secondLineStyle,
                    )
                    if (transaction.description != "") {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "-",
                            style = secondLineStyle,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
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
}