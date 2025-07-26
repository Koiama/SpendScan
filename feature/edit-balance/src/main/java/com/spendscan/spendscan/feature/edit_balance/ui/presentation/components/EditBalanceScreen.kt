package com.spendscan.spendscan.feature.edit_balance.ui.presentation.components

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.spendscan.feature.edit_balance.R
import com.spendscan.spendscan.core.ui.components.FinTrackerTextField
import com.spendscan.spendscan.core.ui.components.ListItem
import com.spendscan.spendscan.core.ui.components.Loading
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenAction
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenAction.ChangeAccountAmount
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenAction.ChangeAccountName
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenAction.ChangeBottomSheetVisibility
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenUiEffect
import com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract.EditScreenUiState
import kotlinx.coroutines.flow.Flow

@Composable
fun EditBalanceScreen(
    modifier: Modifier = Modifier,
    uiState: EditScreenUiState,
    uiEffect: Flow<EditScreenUiEffect>,
    onAction: (EditScreenAction) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        uiEffect.collect { effect ->
            when (effect) {
                is EditScreenUiEffect.ShowError -> {
                    Toast.makeText(context, effect.message, LENGTH_SHORT).show()
                }
                is EditScreenUiEffect.ShowSuccessMessage -> {
                    Toast.makeText(context, effect.message, LENGTH_SHORT).show()
                }
            }
        }
    }
    when {
        uiState.isLoading -> Loading(modifier = modifier.fillMaxSize())
        else -> {
            Column(modifier = modifier) {
                FinTrackerTextField(
                    title = stringResource(R.string.account_name_placeholder),
                    value = uiState.accountName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onValueChange = {
                        if (it.length < 25) {
                            onAction(ChangeAccountName(it))
                        }
                    }
                )
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                FinTrackerTextField(
                    title = stringResource(R.string.balance_placeholder),
                    value = uiState.amountInput,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onValueChange = {
                        onAction(ChangeAccountAmount(it))
                    }
                )
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
                ListItem(
                    content = stringResource(R.string.currency_label),
                    trailingText = uiState.currency.symbol,
                    trailingIcon = ImageVector.vectorResource(R.drawable.forward_arrow_icon),
                    onItemClick = { onAction(ChangeBottomSheetVisibility) },
                    isHighlighted = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            }
        }
    }
}