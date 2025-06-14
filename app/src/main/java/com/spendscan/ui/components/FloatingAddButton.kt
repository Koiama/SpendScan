package com.spendscan.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.spendscan.R

@Composable
fun FloatingAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick =onClick,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 14.dp, end = 16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.button_circle),
                contentDescription = "Добавить доход",
                tint = Color.Unspecified,
                modifier = Modifier.size(56.dp)
            )
        }
    }
}