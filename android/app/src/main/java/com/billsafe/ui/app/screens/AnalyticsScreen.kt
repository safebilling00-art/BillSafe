package com.billsafe.ui.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.billsafe.ui.components.GlassCard

@Composable
fun AnalyticsScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "Analytics", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Where your money goes", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))

        Spacer(modifier = Modifier.height(18.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                BarChart()
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun").forEach { label ->
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(text = "Category Breakdown", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))

                CategoryRow(
                    icon = Icons.Rounded.Tv,
                    iconColor = Color(0xFFEC4899),
                    iconBackground = Color(0xFFEC4899).copy(alpha = 0.2f),
                    label = "Entertainment",
                    percent = 0.60f
                )

                Spacer(modifier = Modifier.height(14.dp))

                CategoryRow(
                    icon = Icons.Rounded.Work,
                    iconColor = Color(0xFF3B82F6),
                    iconBackground = Color(0xFF3B82F6).copy(alpha = 0.2f),
                    label = "Work & SaaS",
                    percent = 0.25f
                )
            }
        }
    }
}

@Composable
private fun BarChart() {
    val heights = listOf(0.40f, 0.65f, 0.35f, 0.85f, 1.00f, 0.20f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        heights.forEachIndexed { idx, frac ->
            val alpha = when (idx) {
                3 -> 0.60f
                4 -> 1.00f
                else -> 0.40f
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height((180.dp * frac))
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha))
            )
        }
    }
}

@Composable
private fun CategoryRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    iconBackground: Color,
    label: String,
    percent: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = label, style = MaterialTheme.typography.bodyMedium)
                Text(text = "${(percent * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color(0xFF1E293B))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(percent)
                        .height(6.dp)
                        .clip(RoundedCornerShape(100))
                        .background(iconColor)
                )
            }
        }
    }
}
