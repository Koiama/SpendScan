package com.spendscan.spendscan.feature.manage_transaction.ui.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.spendscan.spendscan.core.ui.components.DateSelector
import com.spendscan.spendscan.core.ui.components.FinTrackerTextField
import com.spendscan.spendscan.core.ui.components.ListItem
import com.spendscan.feature.manage_transaction.R
import com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.contract.ManageTransactionAction
import com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.contract.ManageTransactionAction.ChangeTimePickerVisibility
import com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.contract.ManageTransactionAction.UpdateDate
import com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.contract.ManageTransactionAction.UpdateTime
import com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.contract.ManageTransactionUiState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Suppress("LongParameterList")
@Composable
fun ManageTransactionScreen(
    uiState: ManageTransactionUiState,
    onAction: (ManageTransactionAction) -> Unit,
    isEditMode: Boolean,
    isIncome: Boolean,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        ListItem(
            content = stringResource(R.string.balance_name_label),
            trailingText = uiState.accountName,
            trailingIcon = ImageVector.vectorResource(R.drawable.forward_arrow_icon),
            modifier = Modifier
                .baseFieldModifier()
        )
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        ListItem(
            content = stringResource(R.string.category_picker_label),
            trailingText = uiState.category.name,
            trailingIcon = ImageVector.vectorResource(R.drawable.forward_arrow_icon),
            onItemClick = { onAction(ManageTransactionAction.ChangeDropdownState) },
            modifier = Modifier
                .baseFieldModifier()
        )
        if (uiState.isDropdownMenuExpanded) {
            FinTrackerDropdown(
                onDismissRequest = { onAction(ManageTransactionAction.ChangeDropdownState) },
                onCategoryClick = { onAction(ManageTransactionAction.UpdateCategory(it)) },
                categories = uiState.categories
            )
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        FinTrackerTextField(
            title = stringResource(R.string.price_field_label),
            value = uiState.stringAmount,
            currencySymbol = uiState.currency.symbol,
            modifier = Modifier.baseFieldModifier(),
            isHighlighted = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            onValueChange = {
                onAction(ManageTransactionAction.UpdateTotalAmount(it))
            }
        )
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        ListItem(
            content = stringResource(R.string.date_picker_label),
            trailingText = uiState.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            onItemClick = { onAction(ManageTransactionAction.ChangeDatePickerVisibility) },
            modifier = Modifier
                .baseFieldModifier()
        )
        if (uiState.isDatePickerShown) {
            DateSelector(
                date = uiState.dateTime.toLocalDate(),
                onDismiss = { onAction(ManageTransactionAction.ChangeDatePickerVisibility) },
                onDateSelected = { newDate ->
                    onAction(UpdateDate(newDate))
                }
            )
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        ListItem(
            content = stringResource(R.string.time_picker_label),
            trailingText = uiState.dateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
            onItemClick = { onAction(ChangeTimePickerVisibility) },
            modifier = Modifier
                .baseFieldModifier()
        )
        if (uiState.isTimePickerShown) {
            TimeSelector(
                onCancel = { onAction(ChangeTimePickerVisibility) },
                updateTimeValue = { hour, minute ->
                    onAction(
                        UpdateTime(LocalTime.of(hour, minute))
                    )
                },
                initialTime = uiState.dateTime.toLocalTime()
            )
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        FinTrackerTextField(
            title = stringResource(R.string.description_field_label),
            isHighlighted = false,
            value = uiState.description,
            modifier = Modifier.baseFieldModifier(),
            onValueChange = {
                onAction(ManageTransactionAction.UpdateDescription(it))
            }
        )
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        if (isEditMode) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(40.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors().copy(containerColor = Color(0xFFE46962)),
                    onClick = {
                        onAction(ManageTransactionAction.DeleteTransaction(onDeleted))
                    }) {
                    Text(
                        "Удалить ${if (isIncome) "доход" else "расход"}",
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                    )
                }
            }
        }
    }
}

fun Modifier.baseFieldModifier(): Modifier = this.then(
    Modifier
        .height(70.dp)
        .fillMaxWidth()
)