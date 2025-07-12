package com.spendscan.spendscan.feature.income.ui.presentation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spendscan.feature.income.R
import com.spendscan.spendscan.core.ui.components.FinTrackerFloatingButton
import com.spendscan.spendscan.core.ui.components.FinTrackerTopBar
import com.spendscan.spendscan.core.ui.components.utils.rememberDaggerViewModel
import com.spendscan.spendscan.feature.income.di.DaggerIncomeComponent
import com.spendscan.spendscan.feature.income.ui.presentation.components.IncomeScreen
import com.spendscan.spendscan.feature.income.ui.viewmodel.IncomeViewModel
import com.spendscan.spendscan.feature.income.ui.viewmodel.contract.IncomeAction

/**
 * Composable обертка для экрана доходов, реализующая базовую структуру экрана
 *
 * @param onTopBarIconClick обработчик клика по иконке в топбаре
 * @param onFloatingButtonClick обработчик клика по плавающей кнопке
 */
@Composable
fun IncomeScreenWrapper(
    onTopBarIconClick: () -> Unit,
    onIncomeClick: (Long) -> Unit,
    onFloatingButtonClick: () -> Unit
) {
    val viewModel: IncomeViewModel = rememberDaggerViewModel(
        createComponent = { coreDeps ->
            DaggerIncomeComponent.factory().create(coreDeps)
        },
        getFactory = { component ->
            component.viewModelFactory()
        }
    )

    LaunchedEffect(Unit) {
        viewModel.onAction(IncomeAction.Refresh)
    }

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            FinTrackerTopBar(
                title = stringResource(R.string.today_income_title),
                trailingIcon = ImageVector.vectorResource(R.drawable.history_icon),
                onTrailingIconClick = onTopBarIconClick
            )
        },
        floatingActionButton = {
            FinTrackerFloatingButton(onFloatingButtonClick)
        }
    ) { innerPadding ->
        IncomeScreen(
            uiState = uiState, modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onIncomeClick = onIncomeClick,
        )
    }
}
