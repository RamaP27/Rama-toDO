package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@Composable
fun BreakdownScreen(
    viewModel: TaskViewModel,
    onNavigateToCopilot: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasksUiState.collectAsStateWithLifecycle()
    var showProfileDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SynapseTopAppBar(
                title = "AI Breakdown",
                subtitle = "Cognitive Tree",
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
            // Hero Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SecondaryContainer.copy(alpha = 0.5f), SurfaceContainer)
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountTree,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Cognitive Task Decomposition",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Synapse breaks ambiguous high-impact commitments into friction-free, actionable steps with automatic time estimations.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = OnSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = onNavigateToCopilot,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                contentColor = OnPrimary
                            ),
                            shape = CircleShape,
                            modifier = Modifier.testTag("deconstruct_new_task_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.size(6.dp))
                            Text("Deconstruct New Task")
                        }
                    }
                }
            }

            // Task Trees
            item {
                Text(
                    text = "Active Decomposition Trees",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                )
            }

            items(tasks, key = { it.task.id }) { item ->
                val task = item.task
                val subtasks = item.subtasks
                val completedCount = subtasks.count { it.isCompleted }
                val progress = if (subtasks.isNotEmpty()) completedCount.toFloat() / subtasks.size else 0f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceContainerLow)
                        .padding(16.dp)
                        .testTag("breakdown_task_item_${task.id}")
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(SurfaceContainerHighest)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${subtasks.size} steps",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Primary)
                                )
                            }
                        }

                        if (subtasks.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = if (progress >= 1f) Tertiary else Primary,
                                trackColor = SurfaceContainerHighest
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                subtasks.forEachIndexed { index, subtask ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(SurfaceContainer)
                                            .clickable { viewModel.toggleSubtask(subtask) }
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(
                                                    if (subtask.isCompleted) PrimaryContainer else SurfaceContainerHighest
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (subtask.isCompleted) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = OnPrimary,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            } else {
                                                Text(
                                                    text = "${index + 1}",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = OnSurfaceVariant,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                            }
                                        }

                                        Text(
                                            text = subtask.title,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = if (subtask.isCompleted) OnSurfaceVariant else OnSurface
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )

                                        Text(
                                            text = "${subtask.estimatedMinutes}m",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Tertiary,
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Single unit task — no subtasks defined",
                                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
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
