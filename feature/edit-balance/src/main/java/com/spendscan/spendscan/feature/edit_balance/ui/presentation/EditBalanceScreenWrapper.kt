package com.spendscan.spendscan.feature.edit_balance.ui.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spendscan.feature.edit_balance.R
import com.spendscan.spendscan.core.ui.components.FinTrackerTopBar
import com.spendscan.spendscan.core.ui.components.utils.rememberDaggerViewModel
import com.spendscan.spendscan.feature.edit_balance.di.DaggerEditBalanceComponent
import com.spendscan.spendscan.feature.edit_balance.ui.presentation.components.EditBalanceScreen
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.EditBalanceViewModel
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenAction.UpdateAccountInfo

@Composable
fun EditBalanceScreenWrapper(
    onCancelButtonClick: () -> Unit,
) {
    val viewModel: EditBalanceViewModel = rememberDaggerViewModel(
        createComponent = { coreDeps ->
            DaggerEditBalanceComponent.factory().create(coreDeps)
        },
        getFactory = { component ->
            component.viewModelFactory()
        }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEffect = viewModel.uiEffect
    Scaffold(
        topBar = {
            FinTrackerTopBar(
                title = uiState.accountName.ifBlank { stringResource(R.string.account_name_default_name) },
                onTrailingIconClick = {
                    (viewModel::onAction)(UpdateAccountInfo(onSuccess = { onCancelButtonClick() }))
                },
                trailingIcon = ImageVector.vectorResource(R.drawable.accept_icon),
                leadingIcon = ImageVector.vectorResource(R.drawable.close_icon),
                onLeadingIconClick = onCancelButtonClick
            )
        }
    ) { innerPadding ->
        EditBalanceScreen(
            uiState = uiState,
            uiEffect = uiEffect,
            onAction = viewModel::onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        )
    }
}