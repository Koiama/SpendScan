package com.spendscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.spendscan.screens.AccountScreen
import com.spendscan.screens.ArticleScreen
import com.spendscan.screens.ExpensesScreen
import com.spendscan.screens.IncomesScreen
import com.spendscan.screens.SettingScreen
import com.spendscan.ui.theme.SpendScanTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendScanTheme {
                SpendScanApp()
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)


@Composable
fun SpendScanApp() {
    val bottomNavItems = listOf(
        BottomNavItem(
            route = "expenses",
            icon = ImageVector.vectorResource(id = R.drawable.expenses_icon),
            label = "Расходы"
        ), BottomNavItem(
            route = "incomes",
            icon = ImageVector.vectorResource(id = R.drawable.incomes_icon),
            label = "Доходы"
        ), BottomNavItem(
            route = "account",
            icon = ImageVector.vectorResource(id = R.drawable.account_icon),
            label = "Счет"
        ), BottomNavItem(
            route = "article",
            icon = ImageVector.vectorResource(id = R.drawable.articles_icon),
            label = "Статьи"
        ),  BottomNavItem(
            route = "settings",
            icon = ImageVector.vectorResource(id = R.drawable.setting_icon),
            label = "Настройки"
        )
    )

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(80.dp)
            ) {
                bottomNavItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem( modifier = Modifier.fillMaxWidth(), //Я не смогла расширить щирину текста, ЧЕРТОВ КОНТЕЙНЕР !!!!!
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label,
                            lineHeight = 16.sp,
                            maxLines = 1,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                        )},
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
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

@Preview(showBackground = true)
@Composable
fun ArticleScreenPreview() {
    SpendScanTheme{SpendScanApp()}
}