package com.spendscan.spendscan.core.ui.components

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.spendscan.ui.R

/**
 * Кастомная плавающая кнопка действия
 * @param onClick Обработчик клика по кнопке
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun FinTrackerFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape,
        modifier = modifier
            .size(56.dp)
            .offset(y = 16.dp)
            .padding(0.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.plus_icon),
            contentDescription = null,
            tint = Color.White
        )
    }
}
