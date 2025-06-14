package com.spendscan.features.account.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendscan.R
import com.spendscan.ui.components.FloatingAddButton
import com.spendscan.ui.components.ListItem
import com.spendscan.ui.components.TopBar

@Composable
fun AccountScreen(modifier: Modifier = Modifier) {
    Box {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopBar(
                    title = "Мой счёт",
                    actionIcon = ImageVector.vectorResource(id = R.drawable.edit_icon),
                    onActionClick = {/*TODO*/})
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                ListItem(
                    modifier = Modifier
                        .height(56.dp),
                    onClick = { /*TODO*/
                    },
                    leadingIconOrEmoji = "\uD83D\uDCB0",
                    leadingIconBgColor = MaterialTheme.colorScheme.onTertiary,
                    itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                    primaryText = "Баланс",
                    trailingText = "-670 000 ₽"
                )

                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

                ListItem(
                    modifier = Modifier
                        .height(56.dp),
                    onClick = { /*TODO*/
                    },
                    itemBackgroundColor = MaterialTheme.colorScheme.onSecondary,
                    primaryText = "Валюта",
                    trailingText = "₽"
                )

                Spacer(
                    Modifier
                        .size(16.dp)
                        .fillMaxWidth()
                )

                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.diagram),
                    contentDescription = "Диаграмма",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        FloatingAddButton(
            onClick = { /*TODO*/}
        )
    }
}

