package com.spendscan.data.models

data class Setting(
    val title: String
)

val settingsList = listOf<Setting>(
    Setting(
        title = "Основной цвет"
    ),
    Setting(
        title = "Звуки"
    ),
    Setting(
        title = "Хаптики"
    ),
    Setting(
        title = "Код пароль"
    ),
    Setting(
        title = "Синхронизация"
    ),
    Setting(
        title = "Язык"
    ),
    Setting(
        title = "О программе"
    )
)