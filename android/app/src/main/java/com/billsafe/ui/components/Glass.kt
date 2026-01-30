package com.billsafe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 24.dp,
    backgroundAlpha: Float = 0.08f,
    borderAlpha: Float = 0.12f,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.White.copy(alpha = backgroundAlpha))
            .border(BorderStroke(1.dp, Color.White.copy(alpha = borderAlpha)), shape),
        content = content
    )
}

@Composable
fun GlassNav(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 0.dp,
    backgroundColor: Color = Color(0xFF0F172A).copy(alpha = 0.7f),
    borderAlpha: Float = 0.12f,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(BorderStroke(1.dp, Color.White.copy(alpha = borderAlpha)), shape),
        content = content
    )
}

