package com.spendscan.spendscan.navigation.flows

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import com.spendscan.spendscan.core.ui.navigation.MainFlowScreen
import com.spendscan.spendscan.core.ui.navigation.Screen
import com.spendscan.spendscan.feature.expenses.ui.presentation.ExpensesScreenWrapper
import com.spendscan.spendscan.feature.history.ui.presentation.HistoryScreenWrapper
import com.spendscan.spendscan.feature.manage_transaction.ui.presentation.ManageTransactionScreenWrapper
import com.spendscan.spendscan.feature.manage_transaction.ui.presentation.navigation.ManageExpenseScreen
import com.spendscan.spendscan.navigation.flows.Expenses.expensesScreen
import com.spendscan.spendscan.navigation.flows.ExpensesHistory.expensesHistoryScreen
import com.spendscan.navigation.R


/**
 * Объект для навигационного потока истории расходов.
 */
@Serializable
data object ExpensesFlow

/**
 * Навигационный граф для экрана истории расходов
 *
 * @param navController контроллер навигации для управления переходами
 */
fun NavGraphBuilder.expensesNavigationFlow(navController: NavHostController) {
    navigation<ExpensesFlow>(Expenses) {
        expensesScreen(
            onTopBarIconClick = {
                navController.navigate(ExpensesHistory)
            },
            onFloatingButtonClick = {
                navController.navigate(ManageExpenseScreen())
            },
            onExpenseClick = {
                navController.navigate(ManageExpenseScreen(it))
            }
        )
        expensesHistoryScreen(
            onLeadingIconClick = {
                navController.navigate(ExpensesFlow) {
                    popUpTo(ExpensesHistory) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onTrailingIconClick = {},
            onItemClick = {
                navController.navigate(ManageExpenseScreen(it))
            }
        )
        expenseManageScreen(
            onCancelButtonClick = {
                navController.navigate(Expenses) {
                    popUpTo(ManageExpenseScreen()) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}

/**
 * Экран расходов
 */
@Serializable
data object Expenses : MainFlowScreen {
    override val icon: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.expenses_icon)
    override val label: String
        @Composable
        get() = stringResource(R.string.expenses_label)

    fun NavGraphBuilder.expensesScreen(
        onTopBarIconClick: () -> Unit,
        onExpenseClick: (Long) -> Unit,
        onFloatingButtonClick: () -> Unit
    ) {
        composable<Expenses> {
            ExpensesScreenWrapper(
                onTopBarIconClick = onTopBarIconClick,
                onExpenseClick = onExpenseClick,
                onFloatingButtonClick = onFloatingButtonClick
            )
        }
    }
}

/**
 * Объект экрана истории расходов, реализующий интерфейс Screen.
 */
@Serializable
data object ExpensesHistory : Screen {
    /**
     * Регистрация composable экрана истории расходов в графе навигации
     *
     * @param onLeadingIconClick обработчик клика по иконке "назад"
     * @param onTrailingIconClick обработчик клика по дополнительной иконке
     */
    fun NavGraphBuilder.expensesHistoryScreen(
        onLeadingIconClick: () -> Unit,
        onItemClick: (Long) -> Unit,
        onTrailingIconClick: () -> Unit
    ) {
        composable<ExpensesHistory> {
            HistoryScreenWrapper(
                onLeadingIconClick = onLeadingIconClick,
                onItemClick = onItemClick,
                onTrailingIconClick = onTrailingIconClick
            )
        }
    }
}

fun NavGraphBuilder.expenseManageScreen(
    onCancelButtonClick: () -> Unit,
) {
    composable<ManageExpenseScreen> { backStackEntry ->
        val transactionData = backStackEntry.toRoute<ManageExpenseScreen>()
        ManageTransactionScreenWrapper(
            transactionId = transactionData.transactionId,
            isIncome = false,
            onCancelButtonClick = onCancelButtonClick
        )
    }
}
