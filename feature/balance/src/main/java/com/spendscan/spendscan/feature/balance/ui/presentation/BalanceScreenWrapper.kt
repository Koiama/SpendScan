package com.spendscan.spendscan.feature.balance.ui.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spendscan.feature.balance.R
import com.spendscan.spendscan.core.ui.components.FinTrackerFloatingButton
import com.spendscan.spendscan.core.ui.components.FinTrackerTopBar
import com.spendscan.spendscan.core.ui.components.utils.rememberDaggerViewModel
import com.spendscan.spendscan.feature.balance.di.DaggerBalanceComponent
import com.spendscan.spendscan.feature.balance.ui.presentation.components.BalanceScreen
import com.spendscan.spendscan.feature.balance.ui.viewmodel.BalanceViewModel

/**
 * Обертка для экрана баланса, интегрирующая UI с ViewModel и Scaffold.
 *
 * @param onTopBarIconClick Обработчик клика по трейлинг иконке в TopBar
 * @param onFloatingButtonClick Обработчик клика по плавающей кнопке
 */
@Composable
fun BalanceScreenWrapper(
    onTopBarIconClick: () -> Unit,
    onFloatingButtonClick: () -> Unit
) {
    val viewModel: BalanceViewModel = rememberDaggerViewModel(
        createComponent = { coreDeps ->
            DaggerBalanceComponent.factory().create(coreDeps)
        },
        getFactory = { component ->
            component.viewModelFactory()
        }
    )
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FinTrackerTopBar(
                onTrailingIconClick = onTopBarIconClick,
                title = uiState.accountName.ifBlank { "Мой счет" },
                trailingIcon = ImageVector.vectorResource(R.drawable.edit_icon)
            )
        },
        floatingActionButton = {
            FinTrackerFloatingButton(onFloatingButtonClick)
        }
    ) { innerPadding ->
        BalanceScreen(
            uiState = uiState, modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
