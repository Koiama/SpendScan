package com.spendscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.spendscan.navigate.SpendScanBottomBar
import com.spendscan.navigate.SpendScanNavGraph
import com.spendscan.ui.theme.SpendScanTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            SpendScanTheme {
                SpendScanApp()
            }
        }
    }
}


@Composable
fun SpendScanApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { SpendScanBottomBar(navController = navController) })
    { innerPadding ->
        SpendScanNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ArticleScreenPreview() {
    SpendScanTheme { SpendScanApp() }
}