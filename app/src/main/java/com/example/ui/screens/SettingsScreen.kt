package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ProfileDialog
import com.example.ui.components.SynapseTopAppBar
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.Tertiary
import com.example.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: TaskViewModel,
    onNavigateToCopilot: () -> Unit,
    modifier: Modifier = Modifier
) {
    var gcalSyncEnabled by remember { mutableStateOf(true) }
    var locationAlertsEnabled by remember { mutableStateOf(true) }
    var autoDeconstructEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showProfileDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SynapseTopAppBar(
                title = "Settings",
                subtitle = "Preferences & AI Engine",
                onAssistantClick = onNavigateToCopilot,
                onProfileClick = { showProfileDialog = true }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Surface,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cognitive AI Engine section
            item {
                Text(
                    text = "AI COGNITIVE ENGINE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = OnSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Text(
                                        text = "Auto NLP Deconstruction",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = OnSurface,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                    Text(
                                        text = "Synthesizes intent into subtasks & timeblocks",
                                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                    )
                                }
                            }

                            Switch(
                                checked = autoDeconstructEnabled,
                                onCheckedChange = { autoDeconstructEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = OnPrimary,
                                    checkedTrackColor = Primary
                                ),
                                modifier = Modifier.testTag("switch_auto_deconstruct")
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = Tertiary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Text(
                                        text = "Google Calendar Sync",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = OnSurface,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                    Text(
                                        text = "Averts scheduling conflicts in real-time",
                                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                    )
                                }
                            }

                            Switch(
                                checked = gcalSyncEnabled,
                                onCheckedChange = { gcalSyncEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = OnPrimary,
                                    checkedTrackColor = Primary
                                ),
                                modifier = Modifier.testTag("switch_gcal_sync")
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Text(
                                        text = "Contextual Location Triggers",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            color = OnSurface,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                    Text(
                                        text = "Notifies when passing stores on route",
                                        style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                    )
                                }
                            }

                            Switch(
                                checked = locationAlertsEnabled,
                                onCheckedChange = { locationAlertsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = OnPrimary,
                                    checkedTrackColor = Primary
                                ),
                                modifier = Modifier.testTag("switch_location_triggers")
                            )
                        }
                    }
                }
            }

            // System & Data section
            item {
                Text(
                    text = "APPLICATION DATA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = OnSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                viewModel.resetToDemoData()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Original demo tasks restored!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceContainerHighest,
                                contentColor = OnSurface
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("reset_demo_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("Reset To Default Demo Tasks")
                        }
                    }
                }
            }
        }
    }

    if (showProfileDialog) {
        ProfileDialog(
            onResetDemoData = { viewModel.resetToDemoData() },
            onDismiss = { showProfileDialog = false }
        )
    }
}
