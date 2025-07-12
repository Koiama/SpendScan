package com.spendscan.spendscan.core.ui.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spendscan.spendscan.core.di.component.CoreComponentDeps
import com.spendscan.spendscan.core.di.component.CoreComponentProvider

@Composable
inline fun <reified CustomType : Any, reified VMType : ViewModel> rememberDaggerViewModel(
    crossinline createComponent: (CoreComponentDeps) -> CustomType,
    crossinline getFactory: (CustomType) -> ViewModelProvider.Factory
): VMType {
    val app = LocalContext.current.applicationContext as CoreComponentProvider
    val coreDeps = app.coreComponent

    val component = remember { createComponent(coreDeps) }
    val factory = remember { getFactory(component) }

    return viewModel(factory = factory)
}
