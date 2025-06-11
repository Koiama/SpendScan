package com.spendscan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ) ,
                title = {
                    Text(
                        text = "Расходы сегодня",
                        fontSize = 28.sp,
                        modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
                    )
                },
                actions = {
                    IconButton( onClick = { /*TODO*/}){
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.history_icon),
                            contentDescription = "История",
                        )
                    }
                },
                modifier = Modifier
                    .height(64.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                shape = CircleShape,
            ){
                Icon(
                    painter = painterResource(id = R.drawable.button_circle),
                    contentDescription = "Добавить расход",
                    tint = Color.Unspecified

                )
            }

        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            Text(
                text = "Экран расходы",
                fontSize = 30.sp
            )
        }
    }
}

