// src/main/java/com/spendscan/features/incomes/myHistory/presentation/DateSelectionSection.kt
package com.spendscan.features.myHistory.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spendscan.core.ui.components.ListItem

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Переиспользуемая функция для форматирования даты для отображения
fun Long.toDateString(): String {
    val instant = Instant.ofEpochMilli(this)
    val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return date.format(formatter)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long? = null
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialSelectedDateMillis)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectionSection(
    startDateMillis: Long?,
    endDateMillis: Long?,
    onStartDateSelected: (Long?) -> Unit,
    onEndDateSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePickerStart by remember { mutableStateOf(false) }
    var showDatePickerEnd by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        ListItem(
            primaryText = "Начало",
            itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .height(55.dp)
                .clickable { showDatePickerStart = true },
            trailingText = startDateMillis?.toDateString() ?: "Выбрать",
            trailingIcon = null,
            onClick = { showDatePickerStart = true }
        )
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
        ListItem(
            primaryText = "Конец",
            itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .height(55.dp)
                .clickable { showDatePickerEnd = true },
            trailingText = endDateMillis?.toDateString() ?: "Выбрать",
            trailingIcon = null,
            onClick = { showDatePickerEnd = true }
        )
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
    }

    // DatePickerModal для начальной даты
    if (showDatePickerStart) {
        DatePickerModal(
            onDateSelected = { dateMillis ->
                onStartDateSelected(dateMillis)
                showDatePickerStart = false
            },
            onDismiss = { showDatePickerStart = false },
            initialSelectedDateMillis = startDateMillis
        )
    }

    // DatePickerModal для конечной даты
    if (showDatePickerEnd) {
        DatePickerModal(
            onDateSelected = { dateMillis ->
                onEndDateSelected(dateMillis)
                showDatePickerEnd = false
            },
            onDismiss = { showDatePickerEnd = false },
            initialSelectedDateMillis = endDateMillis
        )
    }
}