package com.zhengzhou.cashflow.ui.balance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zhengzhou.cashflow.R
import com.zhengzhou.cashflow.customUiElements.CategoryIcon
import com.zhengzhou.cashflow.data.Category
import com.zhengzhou.cashflow.data.Currency
import com.zhengzhou.cashflow.data.Transaction
import com.zhengzhou.cashflow.tools.CashFlowTools
import com.zhengzhou.cashflow.tools.CurrencyFormatter

@Composable
fun SectionCategoryItem(
    amount: Float,
    category: Category,
    currency: Currency,
    onClick: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = { onClick(category) },
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(4.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(
                text = category.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(text = CurrencyFormatter.formatCurrency(currency, amount))
            CategoryIcon(
                iconName = category.iconName,
                contentDescription = category.name
            )
        }
    }
}

@Composable
fun BalanceInSelectedPeriod(
    currency: Currency,
    transactionList: List<Transaction>,
    modifier: Modifier = Modifier,
) {

    val balanceIn = CashFlowTools.balanceFlowIn(transactionList)
    val balanceOut = CashFlowTools.balanceFlowOut(transactionList)

    val formattedIn = CurrencyFormatter.formatCurrency(currency, balanceIn)
    val formattedOut = CurrencyFormatter.formatCurrency(currency, balanceOut)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxHeight()
    ) {
        Text(
            text = stringResource(id = R.string.Balance_selected_period_balance)
        )
        Row {
            Image(
                painter = painterResource(R.drawable.ic_arrow_up),
                colorFilter = ColorFilter.tint(Color.Green),
                contentDescription = null
            )
            Text(
                text = formattedIn,
                color = Color.Green
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(R.drawable.ic_arrow_down),
                colorFilter = ColorFilter.tint(Color.Red),
                contentDescription = null
            )
            Text(
                text = formattedOut,
                color = Color.Red
            )
        }
    }
}