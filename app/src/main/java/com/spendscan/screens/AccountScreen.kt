package com.spendscan.screens

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
import com.spendscan.ui.components.ListItem

@Composable
fun AccountScreen(modifier: Modifier = Modifier) {
    Box {
        Scaffold(
            modifier = modifier,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Мой счёт",
                            lineHeight = 28.sp,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = 0.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 4.dp)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.edit_icon),
                                contentDescription = "Изменить",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
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

                Spacer(Modifier
                    .size(16.dp)
                    .fillMaxWidth())

                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.diagram),
                    contentDescription = "Диаграмма",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        FloatingActionButton(
            onClick = { /*TODO*/ },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 14.dp, end = 16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.button_circle),
                contentDescription = "Добавить счёт",
                tint = Color.Unspecified,
                modifier = Modifier.size(56.dp)
            )
        }
    }
}

