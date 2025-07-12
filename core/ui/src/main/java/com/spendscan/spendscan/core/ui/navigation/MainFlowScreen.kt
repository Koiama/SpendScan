package com.spendscan.spendscan.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Интерфейс для экранов основного потока приложения с нижней навигацией
 */
interface MainFlowScreen : Screen {
    /**
     * Иконка для отображения в нижней навигационной панели
     */
    @get:Composable
    val icon: ImageVector

    /**
     * Текстовая метка для нижней навигационной панели
     */
    @get:Composable
    val label: String
}
