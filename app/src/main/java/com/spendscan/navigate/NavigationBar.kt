package com.spendscan.navigate

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.navigation.compose.currentBackStackEntryAsState

import com.spendscan.R


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
            route = Route.Expenses.route,
            icon = ImageVector.vectorResource(id = R.drawable.expenses_icon),
            label = "Расходы"
        ),
        BottomNavItem(
            route =Route.Incomes.route,
            icon = ImageVector.vectorResource(id = R.drawable.incomes_icon),
            label = "Доходы"
        ),
        BottomNavItem(
            route = Route.Account.route,
            icon = ImageVector.vectorResource(id = R.drawable.account_icon),
            label = "Счет"
        ),
        BottomNavItem(
            route = Route.Article.route,
            icon = ImageVector.vectorResource(id = R.drawable.articles_icon),
            label = "Статьи"
        ),
        BottomNavItem(
            route = Route.Settings.route,
            icon = ImageVector.vectorResource(id = R.drawable.setting_icon),
            label = "Настройки"
        )
    )
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
                        popUpTo(navController.graph.startDestinationId) {
                            saveState =
                                true
                        }
                        launchSingleTop = true
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



