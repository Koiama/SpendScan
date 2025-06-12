package com.spendscan.screens


import android.R.attr.title
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import com.spendscan.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomesScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.primary),
            title = {
                Text(
                    text = "Доходы сегодня",
                    lineHeight = 28.sp
                )
            },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.history_icon),
                            contentDescription = "История",
                        )
                    }
                }
            )
        }
    ){ innerPadding ->

    }
}