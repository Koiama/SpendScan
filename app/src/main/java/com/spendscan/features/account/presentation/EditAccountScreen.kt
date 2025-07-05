package com.spendscan.features.account.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spendscan.R
import com.spendscan.core.ui.components.CurrencySelectionBottomSheet
import com.spendscan.core.ui.components.ListItem
import com.spendscan.core.ui.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Для показа ошибок и уведомлений
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = "Редактировать счёт",
                actionIcon = ImageVector.vectorResource(id = R.drawable.check_icon),
                onActionClick = { viewModel.saveAccount() },
                onBackClick = { onNavigateBack() }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.editedName,
                onValueChange = viewModel::onNameChange,
                label = { Text("Название счета") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.editedBalance,
                onValueChange = viewModel::onBalanceChange,
                label = { Text("Баланс") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { viewModel.showCurrencySelection(true) },
                itemBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                primaryText = "Валюта",
                trailingText = uiState.editedCurrency.symbol
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.saveAccount() },
                enabled = !uiState.isSaving && uiState.account != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Сохранить изменения")
                }
            }
        }

        if (uiState.showCurrencySelection) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.showCurrencySelection(false) },
                sheetState = sheetState
            ) {
                CurrencySelectionBottomSheet(
                    currencies = uiState.availableCurrencies,
                    selectedCurrency = uiState.editedCurrency,
                    onCurrencySelected = { currency ->
                        viewModel.onCurrencySelected(currency)
                    }
                )
            }
        }
    }
}