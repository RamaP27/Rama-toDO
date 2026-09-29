package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.TaskWithSubtasks
import com.example.ui.components.AdjustPlanDialog
import com.example.ui.components.FocusModeDialog
import com.example.ui.components.ProfileDialog
import com.example.ui.components.SmartDigestCard
import com.example.ui.components.SynapseTopAppBar
import com.example.ui.components.TaskCard
import com.example.ui.theme.InversePrimary
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnPrimaryContainer
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
import com.example.ui.theme.Tertiary
import com.example.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@Composable
fun TodayScreen(
    viewModel: TaskViewModel,
    onNavigateToCopilot: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.tasksUiState.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val energyLevel by viewModel.energyLevel.collectAsStateWithLifecycle()
    val isSmartOrderActive by viewModel.isSmartOrderActive.collectAsStateWithLifecycle()
    val isFocusActive by viewModel.focusModeActive.collectAsStateWithLifecycle()
    val focusSecondsRemaining by viewModel.focusSecondsRemaining.collectAsStateWithLifecycle()
    val isFocusPaused by viewModel.isFocusPaused.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showProfileDialog by remember { mutableStateOf(false) }
    var showAdjustPlanDialog by remember { mutableStateOf(false) }
    var showFocusDialog by remember { mutableStateOf(false) }

    val activeTasks = remember(tasks) { tasks.filter { !it.task.isCompleted } }
    val completedTasks = remember(tasks) { tasks.filter { it.task.isCompleted } }

    Scaffold(
        topBar = {
            SynapseTopAppBar(
                title = "Synapse AI",
                subtitle = "Today",
                onAssistantClick = onNavigateToCopilot,
                onProfileClick = { showProfileDialog = true }
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(PrimaryContainer, SecondaryContainer, InversePrimary)
                        )
                    )
                    .clickable(onClick = onNavigateToCopilot)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .testTag("quick_ai_capture_fab")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = OnPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Quick AI Capture",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = OnPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Greeting Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Good morning, Maya",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface,
                                    fontSize = 24.sp
                                )
                            )
                            Text(text = "👋", fontSize = 20.sp)
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Thursday, Oct 24",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = OnSurfaceVariant
                                )
                            )
                        }
                    }

                    // Mini Energy / Status Pill
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh)
                            .clickable { showAdjustPlanDialog = true }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("energy_pill_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "$energyLevel% Energy",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            // AI Daily Smart Digest Card
            item {
                SmartDigestCard(
                    isFocusActive = isFocusActive,
                    focusMinutesRemaining = focusSecondsRemaining / 60,
                    onFocusClick = {
                        if (!isFocusActive) {
                            viewModel.startFocusMode(90)
                        }
                        showFocusDialog = true
                    },
                    onAdjustPlanClick = {
                        showAdjustPlanDialog = true
                    }
                )
            }

            // Filter & Category Chips Horizontal Row
            item {
                val filters = listOf(
                    "All" to "All (${tasks.size})",
                    "High Priority" to "🔥 High Priority",
                    "Work" to "💼 Work",
                    "Personal" to "🌱 Personal",
                    "AI Suggested" to "✨ AI Suggested"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { (key, label) ->
                        val isSelected = selectedFilter == key
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) PrimaryContainer else SurfaceContainerHigh
                                )
                                .clickable { viewModel.selectFilter(key) }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                                .testTag("filter_chip_$key")
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isSelected) OnPrimaryContainer else OnSurfaceVariant,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }

            // Prioritized Tasks Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Prioritized Tasks",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                        )
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SurfaceContainerHighest)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${activeTasks.size} Pending",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceVariant
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .clickable {
                                viewModel.toggleSmartOrder()
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (!isSmartOrderActive) "AI Smart Order activated" else "Standard order"
                                    )
                                }
                            }
                            .padding(4.dp)
                            .testTag("toggle_smart_order_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = null,
                            tint = if (isSmartOrderActive) Primary else OnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "AI Smart Order",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSmartOrderActive) Primary else OnSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // Active Tasks List
            if (activeTasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainer)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No pending tasks in this category",
                            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            } else {
                items(activeTasks, key = { it.task.id }) { item ->
                    TaskCard(
                        taskWithSubtasks = item,
                        onToggleTask = { viewModel.toggleTask(item.task) },
                        onToggleSubtask = { viewModel.toggleSubtask(it) },
                        onDeleteTask = { viewModel.deleteTask(item.task) },
                        onOpenLink = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )
                }
            }

            // Completed Tasks Section
            if (completedTasks.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Completed (${completedTasks.size})",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = OnSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(SurfaceContainerHigh)
                        )
                    }
                }

                items(completedTasks, key = { it.task.id }) { item ->
                    TaskCard(
                        taskWithSubtasks = item,
                        onToggleTask = { viewModel.toggleTask(item.task) },
                        onToggleSubtask = { viewModel.toggleSubtask(it) },
                        onDeleteTask = { viewModel.deleteTask(item.task) },
                        onOpenLink = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    // Dialogs
    if (showProfileDialog) {
        ProfileDialog(
            onResetDemoData = {
                viewModel.resetToDemoData()
                scope.launch { snackbarHostState.showSnackbar("Demo data restored") }
            },
            onDismiss = { showProfileDialog = false }
        )
    }

    if (showAdjustPlanDialog) {
        AdjustPlanDialog(
            currentEnergy = energyLevel,
            onSaveEnergy = { viewModel.updateEnergyLevel(it) },
            onRebalance = {
                scope.launch { snackbarHostState.showSnackbar("Queue rebalanced by Synapse AI") }
            },
            onDismiss = { showAdjustPlanDialog = false }
        )
    }

    if (showFocusDialog) {
        FocusModeDialog(
            secondsRemaining = focusSecondsRemaining,
            isPaused = isFocusPaused,
            onTogglePause = { viewModel.toggleFocusPause() },
            onStop = { viewModel.stopFocusMode() },
            onDismiss = { showFocusDialog = false }
        )
    }
}
