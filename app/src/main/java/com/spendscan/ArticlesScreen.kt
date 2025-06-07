package com.spendscan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun ArticleScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Экран статьи",
            fontSize = 30.sp
        )
    }

}