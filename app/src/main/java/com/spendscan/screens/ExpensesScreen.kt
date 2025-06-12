package com.spendscan.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendscan.R
import com.spendscan.data.models.Expenses
import com.spendscan.ui.components.ListItem
import com.spendscan.ui.theme.SpendScanTheme

val expensesList = listOf(
    Expenses(
        id = "1",
        title = "Аренда квартиры",
        subtitle = null,
        createdAt = "2025-06-12T10:00:00Z",
        trailTag = "",
        trailText = "100 000 ₽",
        iconTag = "\uD83C\uDFE0"
    ),
    Expenses(
        id = "2",
        title = "Одежда",
        subtitle = null,
        createdAt = "2025-06-11T12:30:00Z",
        trailTag = "",
        trailText = "100 000 ₽",
        iconTag = "\uD83D\uDC57"
    ),
    Expenses(
        id = "3",
        title = "На собачку",
        subtitle = "Джек",
        createdAt = "2025-06-10T15:45:00Z",
        trailTag = "",
        trailText = "100 000 ₽",
        iconTag = "\uD83D\uDC36"
    ),
    Expenses(
        id = "4",
        title = "На собачку",
        subtitle = "Энни",
        createdAt = "2025-06-09T09:15:00Z",
        trailTag = "",
        trailText = "100 000 ₽",
        iconTag = "\uD83D\uDC36"
    ),
    Expenses(
        id = "5",
        title = "Ремонт квартиры",
        subtitle = null,
        createdAt = "2025-06-08T11:00:00Z",
        trailTag = "",
        trailText = "100 000 ₽",
        iconTag = "РК"
    ),
    Expenses(
        id = "6",
        title = "Продукты",
        subtitle = null,
        createdAt = "2025-06-07T16:20:00Z",
        trailTag = "",
        trailText = "100 000 ₽",
        iconTag = "\uD83C\uDF6D"
    ),
    Expenses(
        id = "7",
        title = "Спортзал",
        subtitle = null,
        createdAt = "2025-06-06T08:40:00Z",
        trailTag = "",
        trailText = "100 000 ₽",
        iconTag = "\uD83C\uDFCB\uFE0F"
    ),
    Expenses(
        id = "8",
        title = "Медицина",
        subtitle = null,
        createdAt = "2025-06-05T14:00:00Z",
        trailTag = "",
        trailText = "100 000 ₽",
        iconTag = "\uD83D\uDC8A"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = "Расходы сегодня",
                        lineHeight = 28.sp,
                        modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.history_icon),
                            contentDescription = "История",
                        )
                    }
                },
                modifier = Modifier
                    .height(64.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                shape = CircleShape,
                modifier = Modifier
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.button_circle),
                    contentDescription = "Добавить расход",
                    tint = Color.Unspecified
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
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
                    modifier = Modifier
                        .padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "436 558 ₽",
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .padding(end = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            )


            // LazyColumn для отображения списка расходов
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(expensesList) { expense ->
                    ListItem(
                        onClick = { /*TODO*/
                        },
                        leadingIconOrEmoji = expense.iconTag,
                        primaryText = expense.title,
                        secondaryText = expense.subtitle,
                        trailingText = expense.trailText,
                        trailingIcon = ImageVector.vectorResource(R.drawable.drill_in),
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.tertiary)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpensesScreenPreview() {
    SpendScanTheme {
        ExpensesScreen()
    }
}