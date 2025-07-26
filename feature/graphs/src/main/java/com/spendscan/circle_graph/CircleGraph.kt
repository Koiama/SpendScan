package com.spendscan.circle_graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CircleGraph(
    entries: List<CircleGraphElement>,
    modifier: Modifier = Modifier
) {
    val totalSweep = 360f
    val startAngle = -90f
    val strokeWidth = 48f

    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        ) {
            var currentStartAngle = startAngle
            entries.forEach { entry ->
                val sweep = (entry.percentage / 100f) * totalSweep
                drawArc(
                    color = entry.color,
                    startAngle = currentStartAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
                currentStartAngle += sweep
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            entries.forEach { entryData ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(Modifier.size(6.dp)) {
                        drawCircle(entryData.color)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${entryData.percentage.roundToInt()}% ${entryData.label}",
                        fontSize = 10.sp,
                    )
                }
            }
        }
    }
}
