package com.spendscan.spendscan.feature.manage_transaction.ui.viewmodel.contract

import com.spendscan.spendscan.core.domain.models.transaction.Category
import java.time.LocalDate
import java.time.LocalTime


sealed interface ManageTransactionAction {
    data object ChangeDatePickerVisibility: ManageTransactionAction
    data object ChangeTimePickerVisibility: ManageTransactionAction
    data object ChangeDropdownState: ManageTransactionAction
    data class DeleteTransaction(val onSuccess: () -> Unit): ManageTransactionAction
    data class CompleteEdit(val onSuccess: () -> Unit): ManageTransactionAction
    data class UpdateDate(val newDate: LocalDate): ManageTransactionAction
    data class UpdateTotalAmount(val newAmount: String): ManageTransactionAction
    data class UpdateTime(val newTime: LocalTime): ManageTransactionAction
    data class UpdateDescription(val newDescription: String): ManageTransactionAction
    data class UpdateCategory(val newCategory: Category): ManageTransactionAction
}