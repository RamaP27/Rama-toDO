package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.Tertiary

@Composable
fun SmartDigestCard(
    isFocusActive: Boolean,
    focusMinutesRemaining: Int,
    onFocusClick: () -> Unit,
    onAdjustPlanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        SecondaryContainer.copy(alpha = 0.45f),
                        SurfaceContainerHigh,
                        SurfaceContainer
                    )
                )
            )
            .padding(16.dp)
    ) {
        // Atmospheric ambient glow
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = 220.dp, y = (-20).dp)
                .blur(30.dp)
                .background(PrimaryContainer.copy(alpha = 0.25f), CircleShape)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Brain / Neurology Icon in round container
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(SecondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = OnSecondaryContainer,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                // Header: AI SMART DIGEST + Today's Flow
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .alpha(pulseAlpha)
                                .background(Secondary, CircleShape)
                        )
                        Text(
                            text = "AI SMART DIGEST",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Secondary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Text(
                        text = "Today's Flow",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = OnSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Digest narrative
                val digestText = buildAnnotatedString {
                    append("You have ")
                    withStyle(SpanStyle(color = Primary, fontWeight = FontWeight.SemiBold)) {
                        append("2 high-energy tasks")
                    }
                    append(" before lunch. Schedule looks clear for ")
                    withStyle(SpanStyle(color = Tertiary, fontWeight = FontWeight.SemiBold)) {
                        append("90m deep work")
                    }
                    append(" at 10:30 AM.")
                }

                Text(
                    text = digestText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = OnSurface,
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onFocusClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFocusActive) Tertiary else Primary,
                            contentColor = if (isFocusActive) Color(0xFF003640) else OnPrimary
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("focus_mode_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timelapse,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isFocusActive) "Focus (${focusMinutesRemaining}m)" else "Focus Mode",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Button(
                        onClick = onAdjustPlanClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHighest,
                            contentColor = OnSurfaceVariant
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("adjust_plan_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Adjust AI Plan",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}
