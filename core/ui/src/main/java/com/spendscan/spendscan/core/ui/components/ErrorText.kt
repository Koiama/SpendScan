package com.spendscan.spendscan.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Компонент для отображения текста ошибки в центре контейнера
 * @param errorMessage Текст ошибки для отображения
 * @param modifier Модификатор для кастомизации контейнера
 *
 */
@Composable
fun ErrorText(
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.titleLarge.copy(color = Color.Red)
        )
    }
}
