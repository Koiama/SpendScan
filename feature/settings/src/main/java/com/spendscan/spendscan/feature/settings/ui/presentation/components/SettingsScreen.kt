package com.spendscan.spendscan.feature.settings.ui.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spendscan.spendscan.feature.settings.domain.models.SettingItem
import com.spendscan.spendscan.feature.settings.domain.models.SettingType

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val settingsItems = listOf(
            SettingItem("Тёмная тема", SettingType.SWITCH),
            SettingItem("Основной цвет", SettingType.NAVIGATION),
            SettingItem("Звуки", SettingType.NAVIGATION),
            SettingItem("Хаптики", SettingType.NAVIGATION),
            SettingItem("Код пароль", SettingType.NAVIGATION),
            SettingItem("Синхронизация", SettingType.NAVIGATION),
            SettingItem("Язык", SettingType.NAVIGATION),
            SettingItem("О программе", SettingType.NAVIGATION),
        )

        settingsItems.forEach { item ->
            SettingListItem(
                title = item.title,
                type = item.type,
                onClick = {},
                modifier = Modifier.height(56.dp)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
