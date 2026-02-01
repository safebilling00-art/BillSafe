package com.billsafe.ui.app

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.billsafe.ui.app.models.SubscriptionUi
import com.billsafe.ui.app.models.asUi
import com.billsafe.ui.app.screens.AccountScreen
import com.billsafe.ui.app.screens.AlertsScreen
import com.billsafe.ui.app.screens.AnalyticsScreen
import com.billsafe.ui.app.screens.HomeScreen
import com.billsafe.ui.components.BillSafeBackground
import com.billsafe.ui.viewmodels.SubscriptionViewModel
import com.billsafe.ui.viewmodels.SessionViewModel
import com.billsafe.ui.viewmodels.UserSyncViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.billsafe.R

enum class BillSafeTab(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Home("Home", Icons.Rounded.Home),
    Analytics("Stats", Icons.Rounded.PieChart),
    Alerts("Alerts", Icons.Rounded.AccessTime),
    Account("Account", Icons.Rounded.Person),
}

@Composable
fun BillSafeApp() {
    var selectedTab by rememberSaveable { mutableStateOf(BillSafeTab.Home) }

    val sessionViewModel: SessionViewModel = hiltViewModel()
    val firebaseUser by sessionViewModel.firebaseUser.collectAsState()
    val userSyncViewModel: UserSyncViewModel = hiltViewModel()

    var signingIn by remember { mutableStateOf(false) }
    var signInError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val googleSignInClient = remember(context) {
        val webClientId = context.getString(R.string.google_web_client_id).trim()
        val gsoBuilder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()

        if (webClientId.isNotBlank() && webClientId != "REPLACE_ME") {
            gsoBuilder.requestIdToken(webClientId)
        }

        val gso = gsoBuilder.build()

        GoogleSignIn.getClient(context, gso)
    }

    val signInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            signingIn = false
            signInError = "Sign-in was cancelled."
            return@rememberLauncherForActivityResult
        }

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken.isNullOrBlank()) {
                signingIn = false
                signInError = "Google sign-in failed (missing token)."
                return@rememberLauncherForActivityResult
            }

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener { signInTask ->
                    signingIn = false
                    if (!signInTask.isSuccessful) {
                        signInError = signInTask.exception?.message ?: "Google sign-in failed."
                    }
                }
        } catch (e: ApiException) {
            signingIn = false
            signInError = e.localizedMessage ?: "Google sign-in failed."
        }
    }

    val subscriptionViewModel: SubscriptionViewModel = hiltViewModel()
    val subscriptionEntities by subscriptionViewModel.subscriptions.collectAsState(initial = emptyList())
    val subscriptions = remember(subscriptionEntities) { subscriptionEntities.map { it.asUi() } }

    var showAddModal by remember { mutableStateOf(false) }
    var showAllSubscriptions by remember { mutableStateOf(false) }
    var showProfileDetails by remember { mutableStateOf(false) }
    var showSecurity by remember { mutableStateOf(false) }
    var showPremiumPlans by remember { mutableStateOf(false) }

    val userFullName = firebaseUser?.displayName ?: "User"
    val userEmail = firebaseUser?.email.orEmpty()
    val userPhotoUrl = firebaseUser?.photoUrl?.toString()

    val onboardingVisible = firebaseUser == null

    LaunchedEffect(onboardingVisible) {
        if (onboardingVisible) {
            selectedTab = BillSafeTab.Home
        }
    }

    LaunchedEffect(firebaseUser?.uid) {
        if (firebaseUser != null) {
            userSyncViewModel.syncCurrentUserToBackend()
        }
    }

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
                    userFullName = userFullName,
                    userEmail = userEmail,
                    userPhotoUrl = userPhotoUrl,
                    subscriptions = subscriptions,
                    onShowAllSubscriptions = { showAllSubscriptions = true },
                    onShowProfileDetails = { showProfileDetails = true },
                    onShowSecurity = { showSecurity = true },
                    onShowPremiumPlans = { showPremiumPlans = true },
                    onLogout = {
                        sessionViewModel.signOut()
                        googleSignInClient.signOut()
                        signInError = null
                        signingIn = false
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
            onContinueWithGoogle = {
                if (signingIn) return@OnboardingOverlay
                val webClientId = context.getString(R.string.google_web_client_id).trim()
                if (webClientId.isBlank() || webClientId == "REPLACE_ME") {
                    signInError = "Google sign-in isnâ€™t configured yet. Set google_web_client_id in strings.xml."
                    return@OnboardingOverlay
                }
                signInError = null
                signingIn = true
                signInLauncher.launch(googleSignInClient.signInIntent)
            },
            isSigningIn = signingIn,
            errorMessage = signInError
        )
    }
}

@Composable
private fun BillSafeContent(
    paddingValues: PaddingValues,
    selectedTab: BillSafeTab,
    userFullName: String,
    userEmail: String,
    userPhotoUrl: String?,
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
                    userFullName = userFullName,
                    userPhotoUrl = userPhotoUrl,
                    subscriptions = subscriptions,
                    onSeeAll = onShowAllSubscriptions
                )
                BillSafeTab.Analytics -> AnalyticsScreen(paddingValues = paddingValues)
                BillSafeTab.Alerts -> AlertsScreen(paddingValues = paddingValues)
                BillSafeTab.Account -> AccountScreen(
                    paddingValues = paddingValues,
                    userFullName = userFullName,
                    userEmail = userEmail,
                    userPhotoUrl = userPhotoUrl,
                    onShowProfileDetails = onShowProfileDetails,
                    onShowSecurity = onShowSecurity,
                    onShowPremiumPlans = onShowPremiumPlans,
                    onLogout = onLogout
                )
            }
        }
    }
}
