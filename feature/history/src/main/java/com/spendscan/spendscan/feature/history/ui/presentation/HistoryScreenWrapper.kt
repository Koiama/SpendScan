package com.spendscan.spendscan.feature.history.ui.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spendscan.spendscan.core.domain.models.transaction.TransactionType
import com.spendscan.spendscan.core.ui.components.FinTrackerTopBar
import com.spendscan.spendscan.core.ui.components.utils.rememberDaggerViewModel
import com.spendscan.feature.history.R
import com.spendscan.spendscan.feature.history.di.DaggerHistoryComponent
import com.spendscan.spendscan.feature.history.ui.presentation.components.HistoryScreen
import com.spendscan.spendscan.feature.history.ui.viewmodel.HistoryViewModel

/**
 * Обертка для экрана истории транзакций (доходов/расходов)
 * @param onLeadingIconClick Обработчик клика по левой иконке в TopBar
 * @param onTrailingIconClick Обработчик клика по правой иконке в TopBar
 * @param isIncomeScreen Флаг, определяющий тип операций:
 *   - `true`: экран истории доходов
 *   - `false`: экран истории расходов (по умолчанию)
 */
@Composable
fun HistoryScreenWrapper(
    onLeadingIconClick: () -> Unit,
    onTrailingIconClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    isIncomeScreen: Boolean = false
) {
    val viewModel: HistoryViewModel = rememberDaggerViewModel(
        createComponent = { coreDeps ->
            DaggerHistoryComponent.factory().create(coreDeps)
        },
        getFactory = { component ->
            component.viewModelFactory()
        }
    )
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    if (isIncomeScreen) {
        viewModel.transactionType = TransactionType.INCOME
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FinTrackerTopBar(
                title = "Моя история",
                trailingIcon = ImageVector.vectorResource(R.drawable.analysis_icon),
                leadingIcon = ImageVector.vectorResource(R.drawable.back_arrow_icon),
                onTrailingIconClick = onTrailingIconClick,
                onLeadingIconClick = onLeadingIconClick
            )
        }
    ) { innerPadding ->
        HistoryScreen(
            uiState = uiState,
            onAction = viewModel::onAction,
            onItemClick = onItemClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding(), bottom = innerPadding.calculateBottomPadding())
        )
    }
}
