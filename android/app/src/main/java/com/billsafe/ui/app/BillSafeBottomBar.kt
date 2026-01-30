package com.billsafe.ui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.billsafe.ui.components.GlassNav
import com.billsafe.ui.components.pressScaleClick

@Composable
fun BillSafeBottomBar(
    selectedTab: BillSafeTab,
    onTabSelected: (BillSafeTab) -> Unit,
    onAddClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        GlassNav(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            borderAlpha = 0.12f
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(
                    tab = BillSafeTab.Home,
                    selected = selectedTab == BillSafeTab.Home,
                    onClick = { onTabSelected(BillSafeTab.Home) }
                )
                NavItem(
                    tab = BillSafeTab.Analytics,
                    selected = selectedTab == BillSafeTab.Analytics,
                    onClick = { onTabSelected(BillSafeTab.Analytics) }
                )

                Spacer(modifier = Modifier.size(56.dp))

                NavItem(
                    tab = BillSafeTab.Alerts,
                    selected = selectedTab == BillSafeTab.Alerts,
                    onClick = { onTabSelected(BillSafeTab.Alerts) }
                )
                NavItem(
                    tab = BillSafeTab.Account,
                    selected = selectedTab == BillSafeTab.Account,
                    onClick = { onTabSelected(BillSafeTab.Account) }
                )
            }
        }

        AddButton(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-22).dp),
            onClick = onAddClicked
        )
    }
}

@Composable
private fun NavItem(
    tab: BillSafeTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF94A3B8)
    Column(
        modifier = Modifier.pressScaleClick(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.label,
            tint = tint
        )
        Text(
            text = tab.label,
            style = MaterialTheme.typography.labelSmall,
            color = tint
        )
        if (selected) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.primary)
            )
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun AddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = modifier
            .size(56.dp)
            .shadow(elevation = 18.dp, shape = shape, ambientColor = MaterialTheme.colorScheme.primary)
            .clip(shape)
            .background(MaterialTheme.colorScheme.primary)
            .border(width = 4.dp, color = Color(0xFF0F172A), shape = shape)
            .pressScaleClick(pressedScale = 0.9f, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add",
            tint = Color.White
        )
    }
}
