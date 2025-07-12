package com.spendscan.spendscan.core.ui.components

import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Стандартная кнопка для верхнего бара приложения
 *
 * @param onClick Обработчик клика по кнопке
 * @param iconId Идентификатор векторного ресурса для иконки
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun FinTrackerTopBarButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors()
            .copy(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = modifier
    ) {
        content()
    }
}
