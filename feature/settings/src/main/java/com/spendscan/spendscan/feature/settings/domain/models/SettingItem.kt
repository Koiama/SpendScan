package com.spendscan.spendscan.feature.settings.domain.models

enum class SettingType {
    SWITCH, NAVIGATION
}

data class SettingItem(
    val title: String,
    val type: SettingType
)

