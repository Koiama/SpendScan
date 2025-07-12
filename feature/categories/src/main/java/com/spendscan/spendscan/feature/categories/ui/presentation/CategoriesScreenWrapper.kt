package com.spendscan.spendscan.feature.categories.ui.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spendscan.spendscan.core.ui.components.FinTrackerTopBar
import com.spendscan.spendscan.core.ui.components.utils.rememberDaggerViewModel
import com.spendscan.spendscan.feature.categories.di.DaggerCategoriesComponent
import com.spendscan.spendscan.feature.categories.ui.presentation.components.CategoriesScreen
import com.spendscan.spendscan.feature.categories.ui.viewmodel.CategoriesViewModel

/**
 * Обертка для экрана категорий, интегрирующая UI с ViewModel и базовой структурой экрана
 */
@Composable
fun CategoriesScreenWrapper() {
    val viewModel: CategoriesViewModel = rememberDaggerViewModel(
        createComponent = { coreDeps ->
            DaggerCategoriesComponent.factory().create(coreDeps)
        },
        getFactory = { component ->
            component.viewModelFactory()
        }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            FinTrackerTopBar(title = "Мои статьи")
        }
    ) { innerPadding ->
        CategoriesScreen(
            uiState = uiState,
            onAction = viewModel::onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
