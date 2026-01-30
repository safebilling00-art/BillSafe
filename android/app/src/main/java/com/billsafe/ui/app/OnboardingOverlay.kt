package com.billsafe.ui.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billsafe.ui.components.GlassCard
import com.billsafe.ui.components.pressScaleClick

@Composable
fun OnboardingOverlay(
    visible: Boolean,
    onFinish: () -> Unit
) {
    var slide by rememberSaveable { mutableIntStateOf(1) }
    val totalSlides = 3

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(240)),
        exit = fadeOut(tween(700)) + scaleOut(targetScale = 1.1f, animationSpec = tween(700))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
        ) {
            DecorativeBlobs()

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedContent(
                        targetState = slide,
                        transitionSpec = {
                            (slideInHorizontally(tween(320)) { it / 6 } + fadeIn(tween(320))) togetherWith
                                (slideOutHorizontally(tween(260)) { -it / 6 } + fadeOut(tween(180)))
                        },
                        label = "onboardingSlide"
                    ) { step ->
                        when (step) {
                            1 -> OnboardingSlide(
                                icon = Icons.Rounded.Search,
                                accent = Color(0xFF6366F1),
                                title = "Track all your subscriptions",
                                subtitle = "Automatically find and track every recurring payment in one secure dashboard."
                            )

                            2 -> OnboardingSlide(
                                icon = Icons.Rounded.Notifications,
                                accent = Color(0xFF10B981),
                                title = "Get alerts before money is deducted",
                                subtitle = "Stay ahead with smart reminders 48 hours before any auto-debit hits your account."
                            )

                            else -> OnboardingSlide(
                                icon = Icons.Rounded.Savings,
                                accent = Color(0xFFEC4899),
                                title = "Stop wasting money",
                                subtitle = "Identify unused services and cancel them with a single tap to save thousands monthly."
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 28.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ProgressDot(active = slide == 1)
                        Spacer(modifier = Modifier.size(8.dp))
                        ProgressDot(active = slide == 2)
                        Spacer(modifier = Modifier.size(8.dp))
                        ProgressDot(active = slide == 3)
                    }

                    if (slide < totalSlides) {
                        Button(
                            onClick = { slide += 1 },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Next",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            GlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .pressScaleClick(onClick = onFinish)
                                    .padding(0.dp),
                                cornerRadius = 20.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Continue with Google",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White
                                    )
                                }
                            }

                            Text(
                                text = "By continuing, you agree to our Terms",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B),
                                letterSpacing = 1.2.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.DecorativeBlobs() {
    Box(
        modifier = Modifier
            .size(260.dp)
            .offset(x = (-90).dp, y = (-90).dp)
            .clip(RoundedCornerShape(130.dp))
            .background(Color(0xFF6366F1).copy(alpha = 0.18f))
    )
    Box(
        modifier = Modifier
            .size(260.dp)
            .align(Alignment.BottomEnd)
            .offset(x = 90.dp, y = 90.dp)
            .clip(RoundedCornerShape(130.dp))
            .background(Color(0xFFEC4899).copy(alpha = 0.18f))
    )
}

@Composable
private fun OnboardingSlide(
    icon: ImageVector,
    accent: Color,
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(accent.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF94A3B8)
        )
    }
}

@Composable
private fun ProgressDot(active: Boolean) {
    val shape = RoundedCornerShape(50)
    Box(
        modifier = Modifier
            .height(6.dp)
            .size(if (active) 28.dp else 8.dp, 6.dp)
            .clip(shape)
            .background(if (active) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.2f))
    )
}
