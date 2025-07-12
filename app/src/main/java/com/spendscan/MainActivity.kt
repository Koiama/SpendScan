package com.spendscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.spendscan.ui.theme.SpendScanTheme
import com.spendscan.spendscan.navigation.AppNavGraph
import com.spendscan.spendscan.navigation.components.AppBottomBar
import com.spendscan.spendscan.navigation.flows.MainFlow

/**
 *  Главная активити приложения, содержит навигацию
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val routes = MainFlow.routes
            val currentRoute = routes.firstOrNull { route ->
                currentDestination?.hierarchy?.any { it.hasRoute(route::class) } == true
            }
            SpendScanTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(
                            currentRoute != null
                        ) {
                            AppBottomBar(navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        modifier = Modifier
                            .padding(bottom = innerPadding.calculateBottomPadding())
                            .background(MaterialTheme.colorScheme.surface)
                    )
                }
            }
        }
    }
}