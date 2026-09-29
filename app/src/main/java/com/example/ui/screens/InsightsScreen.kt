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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ProfileDialog
import com.example.ui.components.SynapseTopAppBar
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
import com.example.ui.theme.TertiaryContainer
import com.example.ui.viewmodel.TaskViewModel

@Composable
fun InsightsScreen(
    viewModel: TaskViewModel,
    onNavigateToCopilot: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasksUiState.collectAsStateWithLifecycle()
    val energyLevel by viewModel.energyLevel.collectAsStateWithLifecycle()
    var showProfileDialog by remember { mutableStateOf(false) }

    val completedCount = tasks.count { it.task.isCompleted }
    val totalCount = tasks.size

    Scaffold(
        topBar = {
            SynapseTopAppBar(
                title = "Insights & Flow",
                subtitle = "Cognitive Analytics",
                onAssistantClick = onNavigateToCopilot,
                onProfileClick = { showProfileDialog = true }
            )
        },
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
            // Metrics Highlight Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(SecondaryContainer.copy(alpha = 0.5f), SurfaceContainer)
                                )
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$energyLevel%",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                            )
                            Text(
                                text = "Cognitive Energy",
                                style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(TertiaryContainer.copy(alpha = 0.4f), SurfaceContainer)
                                )
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            Icon(
                                imageVector = Icons.Default.SelfImprovement,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "90m",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                            )
                            Text(
                                text = "Deep Work Block",
                                style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(PrimaryContainer.copy(alpha = 0.3f), SurfaceContainer)
                                )
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$completedCount/$totalCount",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                            )
                            Text(
                                text = "Today's Score",
                                style = MaterialTheme.typography.labelSmall.copy(color = OnSurfaceVariant)
                            )
                        }
                    }
                }
            }

            // Energy & Focus Curve
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow)
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Circadian Energy Rhythm",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                            )
                            Text(
                                text = "Peak at 10:30 AM",
                                style = MaterialTheme.typography.labelSmall.copy(color = Tertiary)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Simulated Bar Graph of hourly cognitive focus
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val hours = listOf(
                                "8a" to 0.4f,
                                "9a" to 0.7f,
                                "10a" to 0.95f,
                                "11a" to 0.85f,
                                "12p" to 0.5f,
                                "1p" to 0.4f,
                                "2p" to 0.75f,
                                "3p" to 0.65f,
                                "4p" to 0.55f,
                                "5p" to 0.35f
                            )

                            hours.forEach { (hr, ratio) ->
                                val isPeak = hr == "10a"
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier.height(120.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height((ratio * 90).dp)
                                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                            .background(
                                                if (isPeak) Tertiary else PrimaryContainer.copy(alpha = 0.5f)
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = hr,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isPeak) Tertiary else OnSurfaceVariant,
                                            fontSize = 10.sp,
                                            fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Synapse AI Feedback Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SecondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Synapse AI Insight",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Secondary,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "You consistently execute 40% faster on presentations when scheduled between 10 AM and 12 PM. We automatically designated the 10:30 AM block for your Q4 AI Roadmap Deck.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = OnSurface,
                                    lineHeight = 20.sp
                                )
                            )
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
