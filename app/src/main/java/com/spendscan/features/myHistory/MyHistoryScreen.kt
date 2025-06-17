package com.spendscan.features.myHistory

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.spendscan.R
import com.spendscan.ui.components.TopBar
import androidx.compose.foundation.layout.padding
import com.spendscan.ui.components.ListItem

@Composable
fun MyHistoryScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = "Моя история",
                actionIcon = ImageVector.vectorResource(R.drawable.trailing_icon),
                onActionClick = {/*TODO*/ }
            )
        }
    ) { innearPadding ->
        LazyColumn(modifier = Modifier.padding(top = innearPadding.calculateTopPadding())){


        }
    }
}