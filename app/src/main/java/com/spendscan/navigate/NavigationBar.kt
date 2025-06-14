package com.spendscan.navigate

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.spendscan.R
import com.spendscan.features.account.presentation.AccountScreen
import com.spendscan.features.articles.presentation.ArticleScreen
import com.spendscan.features.expenses.presentation.ExpensesScreen
import com.spendscan.features.incomes.presentation.IncomesScreen
import com.spendscan.features.settings.presentation.SettingScreen


// 1. Data-класс для элементов нижней навигации
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

// 2. Определяем список элементов нижней навигации как константу
@Composable
private fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            route = "expenses",
            icon = ImageVector.vectorResource(id = R.drawable.expenses_icon),
            label = "Расходы"
        ),
        BottomNavItem(
            route = "incomes",
            icon = ImageVector.vectorResource(id = R.drawable.incomes_icon),
            label = "Доходы"
        ),
        BottomNavItem(
            route = "account",
            icon = ImageVector.vectorResource(id = R.drawable.account_icon),
            label = "Счет"
        ),
        BottomNavItem(
            route = "article",
            icon = ImageVector.vectorResource(id = R.drawable.articles_icon),
            label = "Статьи"
        ),
        BottomNavItem(
            route = "settings",
            icon = ImageVector.vectorResource(id = R.drawable.setting_icon),
            label = "Настройки"
        )
    )
}

// 3. Основная Composable функция для всей навигации и Scaffold
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            // Нижняя панель навигации
            SpendScanBottomBar(navController = navController)
        }
    ) { innerPadding ->
        // NavHost для отображения экранов
        NavHost(
            navController,
            startDestination = "expenses",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("account") { AccountScreen() }
            composable("article") { ArticleScreen() }
            composable("incomes") { IncomesScreen() }
            composable("expenses") { ExpensesScreen() }
            composable("settings") { SettingScreen() }
        }
    }
}

@Composable
fun SpendScanBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = getBottomNavItems()
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = {
                    Text(
                        item.label,
                        maxLines = 1,
                        lineHeight = 16.sp,
                        letterSpacing = 0.5.sp,
                        overflow = TextOverflow.Ellipsis, fontSize = 12.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        // Поп до начальной точки графа, чтобы избежать нескольких копий одного и того же destination
                        popUpTo(navController.graph.startDestinationId) {
                            saveState =
                                true // Сохранить состояние Composable для предыдущих экранов
                        }
                        // Избежать нескольких копий одного и того же destination, когда выбрана одна и та же иконка
                        launchSingleTop = true
                        // Восстановить состояние при повторном выборе (если popUpTo(saveState=true))
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.height(80.dp)

            )

        }
    }
}



