package com.billsafe.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.pressScaleClick(
    enabled: Boolean = true,
    pressedScale: Float = 0.97f,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(stiffness = 700f),
        label = "pressScale"
    )

    graphicsLayer {
        scaleX = scale
        scaleY = scale
    }.clickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
    )
}
