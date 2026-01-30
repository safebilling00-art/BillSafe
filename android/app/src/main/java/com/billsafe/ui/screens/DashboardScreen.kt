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
import com.billsafe.data.entities.Bill
import com.billsafe.ui.viewmodels.DashboardViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val bills by viewModel.bills.collectAsState(initial = emptyList())
    var showAddBillDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddBillDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Bill")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "BillSafe",
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            // Monthly Summary Card
            MonthlySummaryCard(bills = bills, modifier = Modifier.padding(16.dp))

            // Bills List
            Text(
                text = "Upcoming Bills",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bills) { bill ->
                    BillItem(bill = bill, onBillClick = {})
                }
            }
        }
    }

    if (showAddBillDialog) {
        AddBillDialog(
            onDismiss = { showAddBillDialog = false },
            onAdd = { bill ->
                viewModel.addBill(bill)
                showAddBillDialog = false
            }
        )
    }
}

@Composable
fun MonthlySummaryCard(bills: List<Bill>, modifier: Modifier = Modifier) {
    val monthlyTotal = bills.filter { it.frequency == "monthly" }.sumOf { it.amount }

    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Monthly Expenses",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "₹${"%.0f".format(monthlyTotal)}",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "${bills.size} active bills",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun BillItem(bill: Bill, onBillClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = bill.billName, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Due: ${bill.dueDate}th", style = MaterialTheme.typography.bodySmall)
            }
            Text(text = "₹${bill.amount}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AddBillDialog(onDismiss: () -> Unit, onAdd: (Bill) -> Unit) {
    var billName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("other") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Bill") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = billName,
                    onValueChange = { billName = it },
                    label = { Text("Bill Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (₹)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date (1-31)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (billName.isNotEmpty() && amount.isNotEmpty() && dueDate.isNotEmpty()) {
                        val bill = Bill(
                            billName = billName,
                            amount = amount.toDouble(),
                            dueDate = dueDate.toInt(),
                            category = category,
                            frequency = "monthly",
                            userId = "user_123"
                        )
                        onAdd(bill)
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
