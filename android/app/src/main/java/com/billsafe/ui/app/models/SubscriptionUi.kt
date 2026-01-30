package com.billsafe.ui.app.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.billsafe.data.entities.Subscription
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

data class SubscriptionUi(
    val name: String,
    val price: Int,
    val next: String,
    val icon: ImageVector,
    val iconBackground: Color,
    val iconTint: Color = Color.White,
    val unused: Boolean
)

fun Subscription.asUi(): SubscriptionUi {
    val meta = iconMetaForName(appName)
    return SubscriptionUi(
        name = appName,
        price = amount.roundToInt(),
        next = formatRenewalDate(renewalDate.toLocalDate()),
        icon = meta.icon,
        iconBackground = meta.background,
        iconTint = meta.tint,
        unused = !isUsed
    )
}

private data class IconMeta(
    val icon: ImageVector,
    val background: Color,
    val tint: Color = Color.White
)

private fun iconMetaForName(name: String): IconMeta {
    val key = name.trim().lowercase()
    return when {
        "netflix" in key -> IconMeta(icon = Icons.Rounded.PlayArrow, background = Color(0xFFEF4444))
        "spotify" in key -> IconMeta(icon = Icons.Rounded.MusicNote, background = Color(0xFF10B981))
        "adobe" in key -> IconMeta(icon = Icons.Rounded.Brush, background = Color(0xFF4F46E5))
        "claude" in key -> IconMeta(
            icon = Icons.Rounded.Bolt,
            background = Color(0xFFFBBF24),
            tint = Color(0xFF0F172A)
        )
        "gym" in key -> IconMeta(icon = Icons.Rounded.FitnessCenter, background = Color(0xFFF97316))
        else -> IconMeta(icon = Icons.Rounded.Autorenew, background = Color(0xFF6366F1))
    }
}

private fun formatRenewalDate(date: LocalDate): String {
    val today = LocalDate.now()
    return when (date) {
        today -> "Today"
        today.plusDays(1) -> "Tomorrow"
        else -> date.format(DateTimeFormatter.ofPattern("MMM d", Locale.getDefault()))
    }
}

