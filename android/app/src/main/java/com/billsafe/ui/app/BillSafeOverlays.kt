package com.billsafe.ui.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billsafe.ui.components.GlassCard
import com.billsafe.ui.components.pressScaleClick
import com.billsafe.ui.app.models.SubscriptionUi
import com.billsafe.ui.app.screens.SubscriptionRow
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun BillSafeOverlays(
    showAddModal: Boolean,
    onDismissAddModal: () -> Unit,
    onAddSubscription: suspend (serviceName: String, cost: Double, cycle: String) -> Boolean,
    showAllSubscriptions: Boolean,
    onDismissAllSubscriptions: () -> Unit,
    subscriptions: List<SubscriptionUi>,
    showProfileDetails: Boolean,
    onDismissProfileDetails: () -> Unit,
    showSecurity: Boolean,
    onDismissSecurity: () -> Unit,
    showPremiumPlans: Boolean,
    onDismissPremiumPlans: () -> Unit
) {
    if (showAddModal) {
        AddSubscriptionDialog(
            onDismiss = onDismissAddModal,
            onAddSubscription = onAddSubscription
        )
    }

    FullScreenOverlay(
        visible = showAllSubscriptions,
        onDismiss = onDismissAllSubscriptions
    ) {
        AllSubscriptionsOverlay(
            subscriptions = subscriptions,
            onBack = onDismissAllSubscriptions
        )
    }

    FullScreenOverlay(
        visible = showProfileDetails,
        onDismiss = onDismissProfileDetails
    ) {
        ProfileDetailsOverlay(onBack = onDismissProfileDetails)
    }

    FullScreenOverlay(
        visible = showSecurity,
        onDismiss = onDismissSecurity
    ) {
        SecurityOverlay(onBack = onDismissSecurity)
    }

    FullScreenOverlay(
        visible = showPremiumPlans,
        onDismiss = onDismissPremiumPlans
    ) {
        PremiumPlansOverlay(onClose = onDismissPremiumPlans)
    }
}

@Composable
private fun FullScreenOverlay(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = visible) {
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(180)),
        exit = fadeOut(tween(160))
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0F172A),
            contentColor = Color.White
        ) {
            content()
        }
    }
}

@Composable
private fun OverlayTopBar(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.pressScaleClick(onClick = onBack),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Back",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ProfileDetailsOverlay(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        OverlayTopBar(title = "Profile", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AW",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Alex Wilson", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Premium Member", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoCard(label = "Email / Apple ID", value = "alex.wilson@icloud.com")
                InfoCard(label = "Phone Number", value = "+91 98765 43210")
                InfoCard(label = "Region", value = "India")
            }
        }
    }
}

@Composable
private fun SecurityOverlay(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        OverlayTopBar(title = "Security & Privacy", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Terms & Conditions", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "By using BillSafe, you agree to our automated tracking of subscription-related SMS and email notifications. We do not sell your personal financial data to third parties.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The service is provided \"as is\". We are not responsible for bank charges incurred due to reminder failures.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Data Protection", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your data is encrypted using AES-256 standards. We use Firebase for secure authentication and cloud storage.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PremiumPlansOverlay(onClose: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.pressScaleClick(onClick = onClose),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Close",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Choose Plan", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Save more by tracking everything.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF94A3B8))

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PlanCard(
                    title = "Annual Savings",
                    price = "₹1,599",
                    subtitle = "/year",
                    badge = "Best Value",
                    highlight = true
                )
                PlanCard(
                    title = "Monthly",
                    price = "₹299",
                    subtitle = "/month",
                    badge = null,
                    highlight = false
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Subscribe Now", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 6.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 120.dp, height = 4.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White.copy(alpha = 0.10f))
            )
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    subtitle: String,
    badge: String?,
    highlight: Boolean
) {
    val borderColor = if (highlight) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.12f)
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 24.dp,
        borderAlpha = 0f
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.08f))
                .padding(18.dp)
        ) {
            Column {
                if (badge != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WorkspacePremium,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = price, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                }
                if (highlight) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Save 55% over monthly", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f))
                } else {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Cancel anytime", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                }
            }
        }
    }
    if (highlight) {
        Spacer(modifier = Modifier.height(0.dp))
    }
}

@Composable
private fun AddSubscriptionDialog(
    onDismiss: () -> Unit,
    onAddSubscription: suspend (serviceName: String, cost: Double, cycle: String) -> Boolean
) {
    var serviceName by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var cycle by remember { mutableStateOf("Monthly") }
    var error by remember { mutableStateOf<String?>(null) }
    var submitting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A).copy(alpha = 0.80f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { },
                cornerRadius = 24.dp
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Text(text = "Track New Subscription", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Label(text = "Service Name")
                    Spacer(modifier = Modifier.height(6.dp))
                    DarkField(
                        value = serviceName,
                        placeholder = "e.g. Netflix, Spotify",
                        onValueChange = { serviceName = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Label(text = "Cost (₹)")
                            Spacer(modifier = Modifier.height(6.dp))
                            DarkField(
                                value = cost,
                                placeholder = "199",
                                onValueChange = { cost = it }
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Label(text = "Cycle")
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                CycleChip(
                                    selected = cycle == "Monthly",
                                    text = "Monthly",
                                    onClick = { cycle = "Monthly" }
                                )
                                CycleChip(
                                    selected = cycle == "Yearly",
                                    text = "Yearly",
                                    onClick = { cycle = "Yearly" }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (submitting) return@Button

                            val name = serviceName.trim()
                            val parsedCost = cost.trim().toDoubleOrNull()

                            error = when {
                                name.isBlank() -> "Please enter a service name."
                                parsedCost == null || parsedCost <= 0.0 -> "Please enter a valid cost."
                                else -> null
                            }

                            if (error != null) return@Button

                            submitting = true
                            scope.launch {
                                val added = onAddSubscription(
                                    name,
                                    parsedCost!!,
                                    cycle
                                )
                                if (added) {
                                    onDismiss()
                                } else {
                                    error = "You're already tracking $name."
                                    submitting = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF0F172A)
                        )
                    ) {
                        Text(
                            text = if (submitting) "Addingâ€¦" else "Add Tracker",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }

                    if (error != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = error!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFB7185)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AllSubscriptionsOverlay(
    subscriptions: List<SubscriptionUi>,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OverlayTopBar(title = "Subscriptions", onBack = onBack)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (subscriptions.isEmpty()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20.dp) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "No subscriptions yet", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "Add a subscription to see it here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            } else {
                items(subscriptions) { sub ->
                    SubscriptionRow(subscription = sub)
                }
            }
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF94A3B8),
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun DarkField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.45f))
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.05f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = Color.White.copy(alpha = 0.10f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun CycleChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    val bg = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.06f)
    val border = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.10f)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(width = 1.dp, color = border, shape = RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = if (selected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.8f)
        )
    }
}
