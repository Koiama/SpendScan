package com.spendscan.features.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendscan.R
import com.spendscan.features.settings.data.settingsList
import com.spendscan.core.ui.components.ListItem
import com.spendscan.core.ui.components.TopBar

@Composable
fun SettingScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar("Настройки")
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
            item {
                Row(
                    modifier = Modifier
                        .height(55.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Тёмная тема",
                        lineHeight = 24.sp,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Switch(
                        checked = false,
                        onCheckedChange = {},
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
            }
            items(settingsList) { setting ->
                ListItem(
                    onClick = { /*TODO*/
                    },
                    primaryText = setting.title,
                    trailingIcon = ImageVector.vectorResource(R.drawable.trailing_element),
                    modifier = Modifier.height(55.dp)
                )
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary)

    }
}