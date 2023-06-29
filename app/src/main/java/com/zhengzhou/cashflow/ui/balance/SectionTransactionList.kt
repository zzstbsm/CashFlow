package com.zhengzhou.cashflow.ui.balance

import android.text.format.DateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.R
import java.text.NumberFormat
import java.util.UUID

@Composable
fun TransactionEntry(
    transactionCategoryGroup: TransactionCategoryGroup,
    currencyFormatter : NumberFormat,
    onClickTransaction: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var category: Category = transactionCategoryGroup.category
    val transaction: Transaction = transactionCategoryGroup.transaction

    if (category.id == UUID(0L,0L)) {
        category = Category(
            name = R.string.no_category,
            idIcon = R.drawable.ic_clear
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .height(60.dp)
            .clickable { onClickTransaction() },
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
            Image(
                painter = painterResource(
                    id = if (category.idIcon == 0) {
                        R.drawable.ic_trending_up
                    } else {
                        category.idIcon
                    }
                ),
                contentDescription = null, //TODO add description
                modifier = modifier
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
                        text = stringResource(id = category.name),
                        style = firstLineStyle,
                        textAlign = TextAlign.Start,
                        modifier = modifier.weight(1f)
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
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = "-",
                        style = secondLineStyle
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Text(
                        text = transaction.description,
                        style = secondLineStyle
                    )
                }
            }
        }
    }
}