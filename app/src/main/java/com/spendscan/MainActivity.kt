package com.spendscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.spendscan.navigate.AppNavigation
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