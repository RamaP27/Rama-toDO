package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.SurfaceContainer

@Composable
fun SynapseTopAppBar(
    title: String = "Synapse AI",
    subtitle: String? = "Today",
    isCopilot: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onAssistantClick: (() -> Unit)? = null,
    onProfileClick: () -> Unit,
    onOptionsClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceContainer.copy(alpha = 0.95f),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Simulated Status Bar matching screenshot
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "9:41",
                    color = OnSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.NetworkCell,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.BatteryFull,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            // Main Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isCopilot && onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.testTag("nav_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = OnSurface
                            )
                        }
                    }

                    SynapseLogo(size = 32.dp)

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Primary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (!isCopilot && onAssistantClick != null) {
                        IconButton(
                            onClick = onAssistantClick,
                            modifier = Modifier.testTag("assistant_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Assistant",
                                tint = Primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    if (isCopilot && onOptionsClick != null) {
                        IconButton(
                            onClick = onOptionsClick,
                            modifier = Modifier.testTag("copilot_options_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options",
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // Profile Avatar with Ring
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, PrimaryContainer.copy(alpha = 0.5f), CircleShape)
                            .clickable(onClick = onProfileClick)
                            .testTag("profile_avatar_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = AVATAR_URL,
                            contentDescription = "Profile Maya",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
    }
}
