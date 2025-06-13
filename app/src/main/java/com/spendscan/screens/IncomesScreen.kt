package com.spendscan.screens

// УДАЛИТЬ ЭТУ СТРОКУ, она вызывает конфликт:
// import android.R.attr.title

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // <--- Убедитесь, что импортирован правильный items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendscan.R
import com.spendscan.data.models.incomesList
import com.spendscan.ui.components.ListItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomesScreen(modifier: Modifier = Modifier) {
    Box {
        Scaffold(
            modifier = modifier,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Доходы сегодня",
                            lineHeight = 28.sp,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 0.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 4.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.history_icon),
                                contentDescription = "История",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                Row(
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSecondary),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        text = "600 000 ₽",
                        lineHeight = 24.sp,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier
                            .padding(end = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(incomesList) { income ->
                        ListItem(
                            onClick = { /*TODO*/ },
                            primaryText = income.title,
                            trailingText = income.trailText,
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { /*TODO*/ },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 14.dp, end = 16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.button_circle),
                contentDescription = "Добавить доход",
                tint = Color.Unspecified,
                modifier = Modifier.size(56.dp)
            )
        }
    }
}