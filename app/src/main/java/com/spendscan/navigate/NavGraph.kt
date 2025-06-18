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
import com.spendscan.features.incomes.myHistory.presentation.MyHistoryScreen
import com.spendscan.features.settings.presentation.SettingScreen
import androidx.navigation.navigation


// 3. Основная Composable функция для всей навигации
@Composable
fun SpendScanNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(
        navController,
        startDestination = Route.Expenses.graphRoute,
        modifier = modifier
    ) {
        composable(Route.Expenses.route) { ExpensesScreen(navController = navController) }
        composable(Route.Incomes.route) { IncomesScreen() }
        composable(Route.Account.route) { AccountScreen() }
        composable(Route.Article.route){ ArticleScreen() }
        composable(Route.Settings.route) { SettingScreen() }

        navigation(
            startDestination = Route.Expenses.route,
            route = Route.Expenses.graphRoute
        ) {
            composable(Route.Expenses.route) { ExpensesScreen(navController = navController)
            }
            composable(Route.MyHistory.route) {
                MyHistoryScreen(navController = navController)
            }
        }

        composable(Route.AddAccount.route) { /* TODO: AddAccountScreen() */ }
    }

}