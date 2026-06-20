package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CeylonGold
import com.example.ui.theme.ForestGreen

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    isAccent: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        border = if (isAccent) null else BorderStroke(1.dp, Color(0xFFE2E8F0)),
        colors = CardDefaults.cardColors(
            containerColor = if (isAccent) ForestGreen else Color.White,
            contentColor = if (isAccent) Color.White else MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isAccent) 6.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2f.sp,
                    color = if (isAccent) CeylonGold else Color(0xFF94A3B8),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isAccent) Color.White.copy(alpha = 0.15f)
                            else ForestGreen.copy(alpha = 0.08f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isAccent) Color.White else ForestGreen,
                lineHeight = 30.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = if (isAccent) Color.White.copy(alpha = 0.7f) else Color(0xFF64748B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CustomBarChart(
    data: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return
    val maxValue = data.maxOrNull() ?: 1.0f

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            val barCount = data.size
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 24.dp.toPx()
            val totalSpacing = spacing * (barCount + 1)
            val barWidth = (canvasWidth - totalSpacing) / barCount

            // Draw guidelines
            for (i in 1..3) {
                val gridY = canvasHeight * (i / 4.0f)
                drawLine(
                    color = Color.LightGray.copy(alpha = 0.4f),
                    start = Offset(0f, gridY),
                    end = Offset(canvasWidth, gridY),
                    strokeWidth = 1.dp.toPx()
                )
            }

            for (index in data.indices) {
                val value = data[index]
                val pct = value / maxValue
                val barHeight = canvasHeight * pct * 0.85f // leave room at top

                val xPos = spacing + index * (barWidth + spacing)
                val yPos = canvasHeight - barHeight

                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(CeylonGold, ForestGreen)
                    ),
                    topLeft = Offset(xPos, yPos),
                    size = Size(barWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx())
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CustomDoughnutChart(
    slices: List<Float>,
    colors: List<Color>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    val total = slices.sum()
    if (total <= 0f) return

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Canvas(modifier = Modifier.size(110.dp)) {
            var startAngle = -90f
            val strokeWidth = 16.dp.toPx()
            val diameter = size.width - strokeWidth
            val rectSize = Size(diameter, diameter)
            val offset = Offset(strokeWidth / 2, strokeWidth / 2)

            for (i in slices.indices) {
                val sweepAngle = (slices[i] / total) * 360f
                drawArc(
                    color = colors.getOrElse(i) { Color.Gray },
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = offset,
                    size = rectSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                startAngle += sweepAngle
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (i in slices.indices) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(colors.getOrElse(i) { Color.Gray })
                    )
                    Text(
                        text = "${labels.getOrNull(i) ?: ""}: ${"%.0f".format((slices[i] / total) * 100)}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp,
            color = ForestGreen
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF64748B) // Slate-500
            )
        }
    }
}

@Composable
fun GoldBadge(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CeylonGold.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreen,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun AlertBanner(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFFFEE2E2)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFEE2E2)),
                contentAlignment = Alignment.Center
            ) {
                Text("!", fontWeight = FontWeight.Bold, color = Color(0xFFDC2626), fontSize = 18.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Low Stock Alert", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7F1D1D))
                Spacer(modifier = Modifier.height(2.dp))
                Text(message, fontSize = 11.sp, color = Color(0xFF991B1B))
            }
        }
    }
}
