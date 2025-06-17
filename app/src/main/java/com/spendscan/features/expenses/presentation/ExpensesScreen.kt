package com.spendscan.features.expenses.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendscan.R
import com.spendscan.features.expenses.expensesList
import com.spendscan.ui.components.FloatingAddButton
import com.spendscan.ui.components.ListItem
import com.spendscan.ui.components.TopBar
import com.spendscan.ui.theme.SpendScanTheme

@Composable
fun ExpensesScreen(modifier: Modifier = Modifier) {
    Box {
        Scaffold(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background),
            topBar = {
                TopBar(
                    title = "Расходы сегодня",
                    actionIcon = ImageVector.vectorResource(id = R.drawable.history_icon),
                    onActionClick = { /**/ } //Пофиксить навигацию
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                // Строка "Всего"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(color = MaterialTheme.colorScheme.onSecondary),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Всего",
                        lineHeight = 24.sp,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier
                            .padding(start = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "436 558 ₽",
                        lineHeight = 24.sp,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier
                            .padding(end = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)


                // LazyColumn для отображения списка расходов
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(expensesList) { expense ->
                        ListItem(
                            onClick = { /*TODO*/
                            },
                            leadingIconOrEmoji = expense.iconTag,
                            primaryText = expense.title,
                            secondaryText = expense.subtitle,
                            trailingText = expense.amount,
                        )

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
        FloatingAddButton(
            onClick = { /*TODO*/ }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpensesScreenPreview() {
    SpendScanTheme {
        ExpensesScreen()
    }
}