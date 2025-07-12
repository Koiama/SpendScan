package com.spendscan.spendscan.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Кастомный верхний бар
 * @param onTrailingIconClick Обработчик клика по трейлинг иконке
 * @param modifier Модификатор для кастомизации
 * @param onLeadingIconClick Обработчик клика по ведущей иконке
 */
@Composable
@Suppress("LongParameterList")
fun FinTrackerTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    leadingIcon: ImageVector? = null,
    onLeadingIconClick: () -> Unit = {},
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Spacer(Modifier.statusBarsPadding())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, bottom = 18.dp, start = 4.dp, end = 4.dp)
        ) {
            FinTrackerTopBarButton(
                onClick = onLeadingIconClick,
                enabled = leadingIcon != null,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                leadingIcon?.let {
                    Icon(imageVector = leadingIcon, contentDescription = null)
                }
            }
            title?.let {
                Text(
                    text = title,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimary)
                )
            }
            FinTrackerTopBarButton(
                onClick = onTrailingIconClick,
                enabled = trailingIcon != null,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                trailingIcon?.let {
                    Icon(imageVector = trailingIcon, contentDescription = null)
                }
            }
        }
    }
}
