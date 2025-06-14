package com.spendscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.spendscan.navigate.AppNavigation
import com.spendscan.navigate.SpendScanBottomBar
import com.spendscan.screens.AccountScreen
import com.spendscan.screens.ArticleScreen
import com.spendscan.screens.ExpensesScreen
import com.spendscan.screens.IncomesScreen
import com.spendscan.screens.SettingScreen
import com.spendscan.ui.theme.SpendScanTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendScanTheme {
                SpendScanApp()
            }
        }
    }
}



@Composable
fun SpendScanApp() {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { AppNavigation()
        }
    ){ innerPadding ->
        
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleScreenPreview() {
    SpendScanTheme{SpendScanApp()}
}