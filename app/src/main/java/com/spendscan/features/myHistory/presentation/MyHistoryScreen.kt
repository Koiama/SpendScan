package com.spendscan.features.myHistory.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.spendscan.R
import com.spendscan.ui.components.TopBar
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavController

@Composable
fun MyHistoryScreen( navController: NavController,
                     modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = "Моя история",
                actionIcon = ImageVector.vectorResource(R.drawable.trailing_icon),
                onActionClick = {/*TODO*/ },
                backButton = ImageVector.vectorResource(R.drawable.back_icon),
                onBackClick = {navController.popBackStack() }
            )
        }
    ) { innearPadding ->
        LazyColumn(modifier = Modifier.padding(top = innearPadding.calculateTopPadding())){


        }
    }
}