package com.billsafe.ui.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.billsafe.ui.components.GlassCard

private data class AlertUi(
    val title: String,
    val body: String
)

@Composable
fun AlertsScreen(paddingValues: PaddingValues) {
    val alerts = remember {
        listOf(
            AlertUi(title = "Netflix renews tomorrow", body = "Reminder 48 hours before auto-debit."),
            AlertUi(title = "2 subscriptions marked unused", body = "Consider canceling to save money."),
            AlertUi(title = "Monthly spend updated", body = "Your total monthly subscriptions: ₹4,289.")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Alerts", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Stay ahead of auto-debits", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
            Spacer(modifier = Modifier.height(14.dp))
        }

        items(alerts) { alert ->
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.NotificationsActive,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(text = alert.title, style = MaterialTheme.typography.titleMedium)
                    }
                    Text(text = alert.body, style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                }
            }
        }
    }
}

