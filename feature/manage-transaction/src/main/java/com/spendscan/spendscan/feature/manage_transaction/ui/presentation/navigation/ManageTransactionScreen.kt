package com.spendscan.spendscan.feature.manage_transaction.ui.presentation.navigation

import kotlinx.serialization.Serializable
import com.spendscan.spendscan.core.ui.navigation.Screen

sealed interface ManageTransactionScreen: Screen

@Serializable
data class ManageIncomeScreen(val transactionId: Long? = null): ManageTransactionScreen

@Serializable
data class ManageExpenseScreen(val transactionId: Long? = null): ManageTransactionScreen