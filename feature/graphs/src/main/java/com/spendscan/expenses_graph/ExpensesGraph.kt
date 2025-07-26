package com.spendscan.expenses_graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.DateTimeFormatter

@Composable
fun ExpensesGraph(
    elements: List<ExpensesGraphElement>,
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFFFF5A00),
    positiveColor: Color = Color(0xFF30D158),
    maxBarHeight: Dp = 150.dp
) {
    val maxAmount = elements.maxOfOrNull { it.amount } ?: 1f

    LazyRow(
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(180.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        items(elements) { element ->
            val barHeight = if (!element.isPositive) {
                maxBarHeight * (element.amount / maxAmount)
            } else {
                4.dp
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(barHeight)
                        .clip(RoundedCornerShape(50))
                        .background(if (element.isPositive) positiveColor else barColor)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = element.date.format(DateTimeFormatter.ofPattern("dd.MM")),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}