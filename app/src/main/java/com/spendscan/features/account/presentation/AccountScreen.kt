package com.spendscan.features.account.presentation

import android.util.Log // <--- ИМПОРТ
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spendscan.R
import com.spendscan.core.ui.components.FloatingAddButton
import com.spendscan.core.ui.components.ListItem
import com.spendscan.core.ui.components.TopBar
import com.spendscan.features.account.domain.models.Currency

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = viewModel(),
    onNavigateToEditAccount: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Логи для отслеживания состояния и перекомпозиции
    Log.d("AccountScreen", "AccountScreen recomposed. uiState: $uiState")
    Log.d("AccountScreen", "AccountScreen - Current account name for TopBar: ${uiState.account?.name ?: "Мой счёт"}")


    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            Log.d("AccountScreen", "Snackbar: ${message}")
        }
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackbarHostState.showSnackbar("Счет успешно обновлен!")
            Log.d("AccountScreen", "Snackbar: Счет успешно обновлен!")
            viewModel.resetSaveSuccess()
        }
    }

    // Перезагрузка счета при возвращении на экран
    LaunchedEffect(Unit) {
        Log.d("AccountScreen", "LaunchedEffect(Unit) triggered. Calling viewModel.loadAccount()")
        viewModel.loadAccount()
    }


    Box {
        Scaffold(
            modifier = modifier,
            topBar = {
                val accountName = uiState.account?.name?.takeIf { it.isNotEmpty() } ?: "Мой счёт"
                Log.d("AccountScreen", "TopBar composable called. Title will be: $accountName")
                TopBar(
                    title = accountName,
                    actionIcon = ImageVector.vectorResource(id = R.drawable.edit_icon),
                    onActionClick = {
                        Log.d("AccountScreen", "Edit icon clicked. Navigating to EditAccountScreen.")
                        onNavigateToEditAccount()
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                if (uiState.isLoading) {
                    Log.d("AccountScreen", "Showing loading indicator.")
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val currentAccount = uiState.account
                    if (currentAccount != null) {
                        Log.d("AccountScreen", "Displaying account details: Name=${currentAccount.name}, Balance=${currentAccount.balance}, Currency=${currentAccount.currency}")
                        val currentCurrencySymbol = Currency.fromCode(currentAccount.currency)?.symbol ?: "???" // Добавим "???" на случай ошибки
                        Log.d("AccountScreen", "Calculated currency symbol: $currentCurrencySymbol")

                        ListItem(
                            modifier = Modifier
                                .height(56.dp),
                            onClick = { /*TODO: Может, показать детали счета?*/ },
                            leadingIconOrEmoji = "\uD83D\uDCB0",
                            leadingIconBgColor = MaterialTheme.colorScheme.onTertiary,
                            itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                            primaryText = currentAccount.name,
                            trailingText = "${currentAccount.balance} $currentCurrencySymbol"
                        )

                        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

                        ListItem(
                            modifier = Modifier
                                .height(56.dp),
                            onClick = { onNavigateToEditAccount() },
                            itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                            primaryText = "Валюта",
                            trailingText = currentCurrencySymbol
                        )

                        Spacer(
                            Modifier
                                .size(16.dp)
                                .fillMaxWidth()
                        )

                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.diagram),
                            contentDescription = "Диаграмма",
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else if (uiState.errorMessage != null) {
                        Log.e("AccountScreen", "Displaying error message: ${uiState.errorMessage}")
                        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                            Text(text = "Не удалось загрузить счет: ${uiState.errorMessage}", color = MaterialTheme.colorScheme.error)
                        }
                    } else {
                        Log.d("AccountScreen", "Account is null and no error message. Initial state or data not loaded yet.")
                        // Возможно, здесь можно показать заглушку или дополнительный индикатор загрузки,
                        // если loadAccount() занимает время и isLoading уже false, но account еще null
                    }
                }
            }
        }
        FloatingAddButton(
            onClick = { /*TODO*/ }
        )
    }
}