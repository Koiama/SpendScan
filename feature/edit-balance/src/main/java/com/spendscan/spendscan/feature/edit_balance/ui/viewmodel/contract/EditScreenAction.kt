package com.spendscan.spendscan.feature.edit_balance.ui.viewmodel.contract

import com.spendscan.spendscan.core.domain.models.account.Currency

sealed interface EditScreenAction{
    data class UpdateAccountInfo(val onSuccess: () -> Unit): EditScreenAction
    data class ChangeAccountAmount(val newAmount: String): EditScreenAction
    data class ChangeAccountName(val newName: String): EditScreenAction
    data class ChangeAccountCurrency(val newCurrency: Currency): EditScreenAction
    data object ChangeBottomSheetVisibility: EditScreenAction
}