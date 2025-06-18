package com.spendscan.features.incomes.myHistory.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.spendscan.R
import com.spendscan.features.incomes.incomesList
import com.spendscan.ui.components.ListItem
import com.spendscan.ui.components.TopBar

//Только история расходов, ручка period

@Composable
fun MyHistoryScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = "Моя история",
                actionIcon = ImageVector.vectorResource(R.drawable.trailing_icon),
                onActionClick = {/*TODO*/ },
                backButton = ImageVector.vectorResource(R.drawable.back_icon),
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innearPadding ->
        Column(modifier = Modifier.padding(top = innearPadding.calculateTopPadding())) {
            ListItem(
                primaryText = "Начало",
                itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.height(55.dp),
                secondaryText = "Февраль 2025"
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
            ListItem(
                primaryText = "Конец",
                itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.height(55.dp),
                secondaryText = "23:41"
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
            ListItem(
                primaryText = "Сумма",
                itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.height(55.dp),
                secondaryText = "125 868 ₽"
            )
            LazyColumn() {
                items(incomesList) { income ->
                    ListItem(
                        onClick = { /*TODO*/ },
                        primaryText = income.title,
                        trailingText = income.amount,
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}