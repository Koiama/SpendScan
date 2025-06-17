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
import com.spendscan.features.myHistory.MyHistoryScreen
import com.spendscan.features.settings.presentation.SettingScreen


// 3. Основная Composable функция для всей навигации
@Composable
fun SpendScanNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(
        navController,
        startDestination = Route.Expenses.route,
        modifier = modifier
    ) {
        composable(Route.Expenses.route) { ExpensesScreen() }
        composable(Route.Incomes.route) { IncomesScreen() }
        composable(Route.Account.route) { AccountScreen() }
        composable(Route.Article.route){ ArticleScreen() }
        composable(Route.Settings.route) { SettingScreen() }
        composable(Route.MyHistory.route) { MyHistoryScreen() }
        composable(Route.AddAccount.route) { /* TODO: AddAccountScreen() */ }
    }

}