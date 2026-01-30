package com.billsafe.ui.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.billsafe.ui.app.models.SubscriptionUi
import com.billsafe.ui.app.models.asUi
import com.billsafe.ui.app.screens.AccountScreen
import com.billsafe.ui.app.screens.AlertsScreen
import com.billsafe.ui.app.screens.AnalyticsScreen
import com.billsafe.ui.app.screens.HomeScreen
import com.billsafe.ui.components.BillSafeBackground
import com.billsafe.ui.viewmodels.SubscriptionViewModel

enum class BillSafeTab(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Home("Home", Icons.Rounded.Home),
    Analytics("Stats", Icons.Rounded.PieChart),
    Alerts("Alerts", Icons.Rounded.AccessTime),
    Account("Account", Icons.Rounded.Person),
}

data class BillSafeUserUi(
    val fullName: String,
    val email: String,
    val photoUrl: String? = null
)

@Composable
fun BillSafeApp() {
    var onboardingVisible by rememberSaveable { mutableStateOf(true) }
    var selectedTab by rememberSaveable { mutableStateOf(BillSafeTab.Home) }

    val user = remember {
        BillSafeUserUi(
            fullName = "Alex Wilson",
            email = "alex.wilson@icloud.com"
        )
    }

    val subscriptionViewModel: SubscriptionViewModel = hiltViewModel()
    val subscriptionEntities by subscriptionViewModel.subscriptions.collectAsState(initial = emptyList())
    val subscriptions = remember(subscriptionEntities) { subscriptionEntities.map { it.asUi() } }

    var showAddModal by remember { mutableStateOf(false) }
    var showAllSubscriptions by remember { mutableStateOf(false) }
    var showProfileDetails by remember { mutableStateOf(false) }
    var showSecurity by remember { mutableStateOf(false) }
    var showPremiumPlans by remember { mutableStateOf(false) }

    BillSafeBackground {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                BillSafeBottomBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    onAddClicked = { showAddModal = true }
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent,
                contentColor = Color.White
            ) {
                BillSafeContent(
                    paddingValues = paddingValues,
                    selectedTab = selectedTab,
                    user = user,
                    subscriptions = subscriptions,
                    onShowAllSubscriptions = { showAllSubscriptions = true },
                    onShowProfileDetails = { showProfileDetails = true },
                    onShowSecurity = { showSecurity = true },
                    onShowPremiumPlans = { showPremiumPlans = true },
                    onLogout = {
                        onboardingVisible = true
                        selectedTab = BillSafeTab.Home
                    }
                )
            }
        }

        BillSafeOverlays(
            showAddModal = showAddModal,
            onDismissAddModal = { showAddModal = false },
            onAddSubscription = { serviceName, cost, cycle ->
                subscriptionViewModel.addSubscriptionIfNew(
                    appName = serviceName,
                    amount = cost,
                    billingCycle = cycle
                )
            },
            showAllSubscriptions = showAllSubscriptions,
            onDismissAllSubscriptions = { showAllSubscriptions = false },
            subscriptions = subscriptions,
            showProfileDetails = showProfileDetails,
            onDismissProfileDetails = { showProfileDetails = false },
            showSecurity = showSecurity,
            onDismissSecurity = { showSecurity = false },
            showPremiumPlans = showPremiumPlans,
            onDismissPremiumPlans = { showPremiumPlans = false }
        )

        OnboardingOverlay(
            visible = onboardingVisible,
            onFinish = { onboardingVisible = false }
        )
    }
}

@Composable
private fun BillSafeContent(
    paddingValues: PaddingValues,
    selectedTab: BillSafeTab,
    user: BillSafeUserUi,
    subscriptions: List<SubscriptionUi>,
    onShowAllSubscriptions: () -> Unit,
    onShowProfileDetails: () -> Unit,
    onShowSecurity: () -> Unit,
    onShowPremiumPlans: () -> Unit,
    onLogout: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                fadeIn(tween(220)) togetherWith fadeOut(tween(140))
            },
            label = "tabContent"
        ) { tab ->
            when (tab) {
                BillSafeTab.Home -> HomeScreen(
                    paddingValues = paddingValues,
                    userFullName = user.fullName,
                    userPhotoUrl = user.photoUrl,
                    subscriptions = subscriptions,
                    onSeeAll = onShowAllSubscriptions
                )
                BillSafeTab.Analytics -> AnalyticsScreen(paddingValues = paddingValues)
                BillSafeTab.Alerts -> AlertsScreen(paddingValues = paddingValues)
                BillSafeTab.Account -> AccountScreen(
                    paddingValues = paddingValues,
                    userFullName = user.fullName,
                    userEmail = user.email,
                    userPhotoUrl = user.photoUrl,
                    onShowProfileDetails = onShowProfileDetails,
                    onShowSecurity = onShowSecurity,
                    onShowPremiumPlans = onShowPremiumPlans,
                    onLogout = onLogout
                )
            }
        }
    }
}
