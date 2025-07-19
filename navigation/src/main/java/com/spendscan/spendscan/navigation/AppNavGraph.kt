package com.spendscan.spendscan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.spendscan.spendscan.navigation.flows.MainFlow
import com.spendscan.spendscan.navigation.flows.balanceNavigationFlow
import com.spendscan.spendscan.navigation.flows.expensesNavigationFlow
import com.spendscan.spendscan.navigation.flows.incomeNavigationFlow
import com.spendscan.spendscan.navigation.flows.mainNavigationFlow

/**
 * Главный навигационный граф приложения
 */
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = MainFlow,
        modifier = modifier
    ) {
        mainNavigationFlow(navController)
        expensesNavigationFlow(navController)
        incomeNavigationFlow(navController)
        balanceNavigationFlow(navController)
    }
}
