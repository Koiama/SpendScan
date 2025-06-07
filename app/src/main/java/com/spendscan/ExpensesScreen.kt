package com.spendscan

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ExpensesScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Экран расходы",
            fontSize = 30.sp
        )
    }
}

