package com.spendscan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spendscan.R
import com.spendscan.ui.theme.SpendScanTheme

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    leadingIconOrEmoji: String? = null,
    leadingIconBgColor: Color = MaterialTheme.colorScheme.onSecondary,
    primaryText: String,
    secondaryText: String? = null,
    trailingText: String? = null,
    trailingIcon: ImageVector? = ImageVector.vectorResource(R.drawable.more_vert),
    itemBackgroundColor: Color = MaterialTheme.colorScheme.background,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = itemBackgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // --- Левая часть: Круглая иконка с цветным фоном ---
            if (leadingIconOrEmoji != null) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(leadingIconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = leadingIconOrEmoji, fontSize = 18.sp, fontWeight = FontWeight.Medium
                    )
                }
            }

            // --- Центральная часть: Основной и дополнительный текст ---
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = primaryText,
                    maxLines = 1,
                    lineHeight = 24.sp,
                    fontSize = 16.sp,
                    letterSpacing = 0.5.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                secondaryText?.let {
                    Text(
                        text = it,
                        maxLines = 1,
                        lineHeight = 20.sp,
                        fontSize = 14.sp,
                        letterSpacing = 0.25.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // --- Правая часть: Сумма и стрелка  ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                if (trailingText != null) {
                    Text(
                        trailingText, maxLines = 1, color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (trailingIcon != null) {
                    Icon(
                        trailingIcon, contentDescription = null, modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ListItemPreview() {
    SpendScanTheme {
        Column {
            ListItem(
                leadingIconOrEmoji = "🏠",
                primaryText = "Заголовок",
                secondaryText = "Дополнительный текст",
                trailingText = "100 000 ₽",
                trailingIcon = ImageVector.vectorResource(R.drawable.more_vert),
                onClick = { /* Логика клика */ })
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            ListItem(
                leadingIconOrEmoji = "🏠",
                primaryText = "Домик",
                secondaryText = null,
                trailingText = "50 000 ₽",
                trailingIcon = Icons.AutoMirrored.Default.ArrowBack,
                onClick = { /* Логика клика */ })
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            ListItem(
                leadingIconOrEmoji = "П",
                primaryText = "Потратил куда-то",
                secondaryText = null,
                trailingText = "12 000 ₽",
                trailingIcon = Icons.AutoMirrored.Default.ArrowBack,
                onClick = { /* Логика клика */ })
        }
    }
}