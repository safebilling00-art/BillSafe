package com.billsafe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.billsafe.data.entities.Subscription
import com.billsafe.ui.viewmodels.SubscriptionViewModel

@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    val subscriptions by viewModel.subscriptions.collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Subscription")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Subscriptions",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )

            if (subscriptions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No active subscriptions")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(subscriptions) { subscription ->
                        SubscriptionItem(subscription)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddSubscriptionDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { subscription ->
                viewModel.addSubscription(subscription)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun SubscriptionItem(subscription: Subscription) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = subscription.appName, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Renews: ${subscription.renewalDate}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(text = "₹${subscription.amount}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AddSubscriptionDialog(onDismiss: () -> Unit, onAdd: (Subscription) -> Unit) {
    var appName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Subscription") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = appName,
                    onValueChange = { appName = it },
                    label = { Text("App/Service Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (appName.isNotEmpty() && amount.isNotEmpty()) {
                        val subscription = Subscription(
                            appName = appName,
                            amount = amount.toDouble(),
                            userId = "user_123",
                            billingCycle = "monthly",
                            startDate = java.time.LocalDateTime.now(),
                            renewalDate = java.time.LocalDateTime.now().plusMonths(1)
                        )
                        onAdd(subscription)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
