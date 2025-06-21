package com.spendscan.navigate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spendscan.features.account.presentation.AccountScreen
import com.spendscan.features.articles.presentation.ArticleScreen
import com.spendscan.features.expenses.presentation.ExpensesScreen
import com.spendscan.features.incomes.presentation.IncomesScreen
import com.spendscan.features.settings.presentation.SettingScreen
import androidx.navigation.navigation
import com.spendscan.features.expenses.myHistory.presentation.MyHistoryScreen
import com.spendscan.features.expenses.myHistory.presentationIncomes.MyHistoryIncomesScreen


// 3. Основная Composable функция для всей навигации
@Composable
fun SpendScanNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(
        navController,
        startDestination = Route.Expenses.graphRoute,
        modifier = modifier
    ) {
        composable(Route.Account.route) { AccountScreen() }
        composable(Route.Article.route){ ArticleScreen() }
        composable(Route.Settings.route) { SettingScreen() }

        // --- ГРАФ РАСХОДОВ ---
        navigation(
            startDestination = Route.Expenses.route, // Внутри графа, Expenses.route - это стартовая точка
            route = Route.Expenses.graphRoute // Маршрут самого графа
        ) {
            composable(Route.Expenses.route) { ExpensesScreen(navController = navController) }
            composable(Route.MyHistoryExpenses.route) {
                MyHistoryScreen(navController = navController)
            }
        }

        // --- ГРАФ ДОХОДОВ ---
        navigation(
            startDestination = Route.Incomes.route, // Внутри графа, Incomes.route - это стартовая точка
            route = Route.Incomes.graphRoute // Маршрут самого графа
        ) {
            composable(Route.Incomes.route) { IncomesScreen(navController = navController) }
            composable(Route.MyHistoryIncomes.route) {
                MyHistoryIncomesScreen(navController = navController)
            }
        }


        composable(Route.AddAccount.route) { /* TODO: AddAccountScreen() */ }
    }

}