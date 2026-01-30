package com.billsafe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun UserAvatar(
    fullName: String,
    photoUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    cornerRadius: Dp = 14.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {
    val initials = initialsFromName(fullName)
    val shape = RoundedCornerShape(cornerRadius)

    if (!photoUrl.isNullOrBlank()) {
        SubcomposeAsyncImage(
            model = photoUrl,
            contentDescription = null,
            modifier = modifier
                .size(size)
                .clip(shape)
                .background(backgroundColor),
            contentScale = ContentScale.Crop,
            loading = { AvatarFallback(initials = initials, backgroundColor = backgroundColor, shape = shape, modifier = Modifier.fillMaxSize()) },
            error = { AvatarFallback(initials = initials, backgroundColor = backgroundColor, shape = shape, modifier = Modifier.fillMaxSize()) }
        )
    } else {
        AvatarFallback(
            initials = initials,
            backgroundColor = backgroundColor,
            shape = shape,
            modifier = modifier.size(size)
        )
    }
}

@Composable
private fun AvatarFallback(
    initials: String,
    backgroundColor: Color,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

private fun initialsFromName(fullName: String): String {
    val parts = fullName
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }

    if (parts.isEmpty()) return "?"

    val first = parts.first().firstOrNull()?.uppercaseChar()?.toString().orEmpty()
    val second = if (parts.size >= 2) {
        parts[1].firstOrNull()?.uppercaseChar()?.toString().orEmpty()
    } else {
        parts.first().drop(1).firstOrNull()?.uppercaseChar()?.toString().orEmpty()
    }

    val initials = (first + second).ifBlank { "?" }
    return initials.take(2)
}
