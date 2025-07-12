package com.spendscan.spendscan.feature.settings.domain.models

/**
 * Тип элемента настройки.
 */
enum class SettingType {
    SWITCH, NAVIGATION
}

/**
 * Модель элемента настроек.
 */
data class SettingItem(
    val title: String,
    val type: SettingType
)

