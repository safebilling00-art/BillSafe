package com.billsafe.ui.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billsafe.ui.components.GlassCard
import com.billsafe.ui.components.UserAvatar
import com.billsafe.ui.components.pressScaleClick

@Composable
fun AccountScreen(
    paddingValues: PaddingValues,
    userFullName: String,
    userEmail: String,
    userPhotoUrl: String?,
    onShowProfileDetails: () -> Unit,
    onShowSecurity: () -> Unit,
    onShowPremiumPlans: () -> Unit,
    onLogout: () -> Unit
) {
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "Account", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        ProfileOverviewCard(
            fullName = userFullName,
            email = userEmail,
            photoUrl = userPhotoUrl,
            onClick = onShowProfileDetails
        )

        Spacer(modifier = Modifier.height(14.dp))

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                SettingsRow(
                    icon = Icons.Rounded.WorkspacePremium,
                    iconTint = Color(0xFFFBBF24),
                    label = "Premium Plan",
                    trailing = {
                        Text(
                            text = "Manage",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = onShowPremiumPlans
                )

                DividerLine()

                SettingsRow(
                    icon = Icons.Rounded.Notifications,
                    iconTint = Color.White,
                    label = "Notifications",
                    trailing = {
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = Color.White.copy(alpha = 0.8f),
                                uncheckedTrackColor = Color.White.copy(alpha = 0.15f)
                            )
                        )
                    },
                    onClick = { notificationsEnabled = !notificationsEnabled }
                )

                DividerLine()

                SettingsRow(
                    icon = Icons.Rounded.Security,
                    iconTint = Color.White,
                    label = "Security & Privacy",
                    trailing = {
                        Icon(
                            imageVector = Icons.Rounded.ChevronRight,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8)
                        )
                    },
                    onClick = onShowSecurity
                )

                DividerLine()

                SettingsRow(
                    icon = Icons.Rounded.Logout,
                    iconTint = Color(0xFFFB7185),
                    label = "Log Out",
                    labelColor = Color(0xFFFB7185),
                    trailing = {},
                    onClick = onLogout
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "BillSafe v1.0.4 • Made for iOS",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF64748B)
        )
    }
}

@Composable
private fun ProfileOverviewCard(
    fullName: String,
    email: String,
    photoUrl: String?,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .pressScaleClick(onClick = onClick),
        cornerRadius = 20.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(
                fullName = fullName,
                photoUrl = photoUrl,
                size = 56.dp,
                cornerRadius = 18.dp
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = fullName, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = email, style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
            }

            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF475569)
            )
        }
    }
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    labelColor: Color = Color.White,
    trailing: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .pressScaleClick(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint)
            Text(text = label, style = MaterialTheme.typography.bodyLarge, color = labelColor)
        }
        trailing()
    }
}

@Composable
private fun DividerLine() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.06f))
    )
}
