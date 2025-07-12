package com.spendscan.spendscan.core.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.spendscan.spendscan.core.ui.components.utils.toLocalDate
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Компонент выбора даты с диалоговым окном календаря
 *
 * @param date Выбранная дата
 * @param onDateSelected Обработчик выбора новой даты
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(millis.toLocalDate())
                    }
                    onDismiss()
                }
            ) {
                Text("OK", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = Color.Black)
            }
        },
        colors = DatePickerDefaults.colors().copy(containerColor = Color(0xFFD4FAE6))
    ) {
        DatePicker(
            state = datePickerState,
            title = null,
            headline = null,
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFFD4FAE6),
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                selectedDayContentColor = Color.Black,
                todayContentColor = Color.Black,
                navigationContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }
}
