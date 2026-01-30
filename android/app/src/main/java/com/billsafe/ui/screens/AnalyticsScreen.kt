package com.billsafe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.billsafe.data.entities.Bill
import com.billsafe.ui.viewmodels.AnalyticsViewModel
import java.time.LocalDateTime

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val bills by viewModel.bills.collectAsState(initial = emptyList())
    val monthlyTotal = bills.filter { it.frequency == "monthly" }.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Analytics & Insights",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Monthly Expense Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Monthly Expenses", style = MaterialTheme.typography.titleMedium)
                Text("₹${"%.0f".format(monthlyTotal)}", style = MaterialTheme.typography.headlineMedium)
            }
        }

        // Category Breakdown
        Text(
            text = "By Category",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val categoryBreakdown = bills.groupingBy { it.category }.eachCount()
        categoryBreakdown.forEach { (category, count) ->
            CategoryItem(category, count)
        }
    }
}

@Composable
fun CategoryItem(category: String, count: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(category.uppercase())
            Text("$count bills")
        }
    }
}
