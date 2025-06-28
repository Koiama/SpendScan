package com.spendscan.navigate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.spendscan.features.myHistory.presentation.TransactionListScreen
import com.spendscan.features.account.presentation.AccountScreen
import com.spendscan.features.articles.presentation.ArticleScreen
import com.spendscan.features.expenses.presentation.ExpensesScreen
import com.spendscan.features.incomes.presentation.IncomesScreen
import com.spendscan.features.settings.presentation.SettingScreen


@Composable
fun SpendScanNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(
        navController = navController,
        startDestination = Route.Expenses.route,
        modifier = modifier
    ) {
        composable(Route.Expenses.route) {
            ExpensesScreen(navController = navController)
        }

        composable(Route.Incomes.route) {
            IncomesScreen(navController = navController)
        }

        composable(
            route = Route.MyHistory.route,
            arguments = listOf(
                navArgument("isIncome") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = "null"
                },
                navArgument("title") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val isIncomeString = backStackEntry.arguments?.getString("isIncome")
            val isIncome = when (isIncomeString) {
                "true" -> true
                "false" -> false
                else -> null
            }
            val title = backStackEntry.arguments?.getString("title") ?: "История транзакций"
            val decodedTitle = java.net.URLDecoder.decode(title, "UTF-8")

            TransactionListScreen(
                navController = navController,
                isIncome = isIncome,
                title = decodedTitle
            )
        }

        // Other single screens
        composable(Route.Account.route) { AccountScreen() }
        composable(Route.Article.route) { ArticleScreen() }
        composable(Route.Settings.route) { SettingScreen() }
    }
}