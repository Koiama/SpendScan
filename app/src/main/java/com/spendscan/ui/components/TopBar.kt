package com.spendscan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding

@Composable
fun TopBar(
    title: String,
    // Опциональная иконка и опциональный обработчик клика
    actionIcon: ImageVector? = null, // Иконка может быть null
    onActionClick: (() -> Unit)? = null // Действие может быть null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        // Используем Box для наложения элементов
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title,
                lineHeight = 28.sp,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 0.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )

            if (actionIcon != null && onActionClick != null) {
                IconButton(
                    onClick = onActionClick, // Используем переданный onClick
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                ) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = "Действие",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}