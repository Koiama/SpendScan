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
import com.spendscan.spendscan.feature.history.ui.presentation.HistoryScreenWrapper
import com.spendscan.spendscan.feature.income.ui.presentation.IncomeScreenWrapper
import com.spendscan.spendscan.feature.manage_transaction.ui.presentation.ManageTransactionScreenWrapper
import com.spendscan.spendscan.feature.manage_transaction.ui.presentation.navigation.ManageIncomeScreen
import com.spendscan.spendscan.navigation.flows.Income.incomeScreen
import com.spendscan.spendscan.navigation.flows.IncomeHistory.incomeHistoryScreen
import com.spendscan.navigation.R

/**
 * Объект для навигационного потока истории доходов.
 */
@Serializable
data object IncomeFLow

/**
 * Навигационный граф для экрана истории доходов.
 *
 * @param navController контроллер навигации для управления переходами
 */
fun NavGraphBuilder.incomeNavigationFlow(navController: NavHostController) {
    navigation<IncomeFLow>(Income) {
        incomeScreen(
            onTopBarIconClick = {
                navController.navigate(IncomeHistory)
            },
            onFloatingButtonClick = {
                navController.navigate(ManageIncomeScreen())
            },
            onIncomeClick = {
                navController.navigate(ManageIncomeScreen(it))
            }
        )
        incomeHistoryScreen(
            onLeadingIconClick = {
                navController.navigate(Income) {
                    popUpTo(IncomeHistory) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            onTrailingIconClick = {},
            onItemClick = {navController.navigate(ManageIncomeScreen(it))}
        )
        incomeManageScreen(
            onCancelButtonClick = {
                navController.navigate(Income) {
                    popUpTo(ManageIncomeScreen()) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        )
    }
}

/**
 * Экран доходов
 */
@Serializable
data object Income : MainFlowScreen {
    override val icon: ImageVector
        @Composable
        get() = ImageVector.vectorResource(R.drawable.income_icon)
    override val label: String
        @Composable
        get() = stringResource(R.string.income_label)

    fun NavGraphBuilder.incomeScreen(
        onTopBarIconClick: () -> Unit,
        onIncomeClick: (Long) -> Unit,
        onFloatingButtonClick: () -> Unit
    ) {
        composable<Income> {
            IncomeScreenWrapper(
                onTopBarIconClick = onTopBarIconClick,
                onIncomeClick = onIncomeClick,
                onFloatingButtonClick = onFloatingButtonClick
            )
        }
    }
}

/**
 * Объект экрана истории доходов, реализующий интерфейс Screen.
 */
@Serializable
data object IncomeHistory : Screen {
    /**
     * Регистрация composable экрана истории доходов в графе навигации
     *
     * @param onLeadingIconClick обработчик клика по иконке "назад"
     * @param onTrailingIconClick обработчик клика по дополнительной иконке
     */
    fun NavGraphBuilder.incomeHistoryScreen(
        onLeadingIconClick: () -> Unit,
        onItemClick: (Long) -> Unit,
        onTrailingIconClick: () -> Unit
    ) {
        composable<IncomeHistory> {
            HistoryScreenWrapper(
                isIncomeScreen = true,
                onItemClick = onItemClick,
                onLeadingIconClick = onLeadingIconClick,
                onTrailingIconClick = onTrailingIconClick
            )
        }
    }
}


fun NavGraphBuilder.incomeManageScreen(
    onCancelButtonClick: () -> Unit
) {
    composable<ManageIncomeScreen> { backStackEntry ->
        val transactionData = backStackEntry.toRoute<ManageIncomeScreen>()
        ManageTransactionScreenWrapper(
            transactionId = transactionData.transactionId,
            isIncome = true,
            onCancelButtonClick = onCancelButtonClick
        )
    }
}