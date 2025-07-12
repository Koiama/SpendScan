package com.spendscan.spendscan.feature.settings.ui.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.spendscan.spendscan.core.ui.components.FinTrackerTopBar
import com.spendscan.spendscan.feature.settings.ui.presentation.components.SettingsScreen

@Composable
fun SettingsScreenWrapper() {
    Scaffold(
        topBar = {
            FinTrackerTopBar(title = "Настройки")
        }
    ) { innerPadding ->
        SettingsScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
