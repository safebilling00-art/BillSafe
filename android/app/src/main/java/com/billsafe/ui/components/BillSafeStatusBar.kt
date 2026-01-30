package com.billsafe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.SignalCellular4Bar
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun BillSafeStatusBar(modifier: Modifier = Modifier) {
    var timeText by remember { mutableStateOf("09:41") }
    val formatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    LaunchedEffect(Unit) {
        while (true) {
            timeText = LocalTime.now().format(formatter)
            delay(1_000)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = timeText,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.SignalCellular4Bar,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
            )
            Icon(
                imageVector = Icons.Rounded.Wifi,
                contentDescription = null,
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Rounded.BatteryFull,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

