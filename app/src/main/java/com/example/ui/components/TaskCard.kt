package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SubTaskEntity
import com.example.data.TaskWithSubtasks
import com.example.ui.theme.Error
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.OnErrorContainer
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
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryContainer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskCard(
    taskWithSubtasks: TaskWithSubtasks,
    onToggleTask: () -> Unit,
    onToggleSubtask: (SubTaskEntity) -> Unit,
    onDeleteTask: () -> Unit,
    onOpenLink: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val task = taskWithSubtasks.task
    val subtasks = taskWithSubtasks.subtasks
    var isExpanded by remember { mutableStateOf(false) }
    var menuOpen by remember { mutableStateOf(false) }

    val cardBgColor by animateColorAsState(
        targetValue = if (task.isCompleted) SurfaceContainerLowest else SurfaceContainerLow,
        label = "cardBg"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBgColor)
            .alpha(if (task.isCompleted) 0.72f else 1f)
            .padding(14.dp)
            .testTag("task_card_${task.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Checkbox
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (task.isCompleted) Primary else SurfaceContainerHighest
                    )
                    .clickable(onClick = onToggleTask)
                    .testTag("task_checkbox_${task.id}"),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = OnPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Main Content
            Column(modifier = Modifier.weight(1f)) {
                // Title and 3-dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (task.isCompleted) OnSurfaceVariant else OnSurface,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Box {
                        if (task.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified completed",
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            IconButton(
                                onClick = { menuOpen = true },
                                modifier = Modifier
                                    .size(24.dp)
                                    .testTag("task_menu_${task.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options",
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = menuOpen,
                                onDismissRequest = { menuOpen = false },
                                modifier = Modifier.background(SurfaceContainerHigh)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Delete Task", color = Error) },
                                    onClick = {
                                        menuOpen = false
                                        onDeleteTask()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Toggle Completion", color = OnSurface) },
                                    onClick = {
                                        menuOpen = false
                                        onToggleTask()
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Tags / Metadata chips
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Category chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHighest)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = task.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    // Priority chip if High
                    if (task.priority.contains("High", ignoreCase = true)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ErrorContainer)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PriorityHigh,
                                contentDescription = null,
                                tint = OnErrorContainer,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = task.priority,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnErrorContainer,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    } else if (task.priority.contains("Medium", ignoreCase = true)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SecondaryContainer.copy(alpha = 0.4f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Medium",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSecondaryContainer,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }

                    // Estimated duration
                    if (!task.estimatedTime.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerHigh)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = task.estimatedTime,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Tertiary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }

                    // Extra meta (e.g. 14 files changed)
                    if (!task.extraMeta.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerHigh)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = task.extraMeta,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceVariant
                                )
                            )
                        }
                    }

                    // Location Note pill
                    if (!task.locationNote.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(TertiaryContainer.copy(alpha = 0.25f))
                                .padding(horizontal = 9.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NearMe,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = task.locationNote,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Tertiary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                // Subtasks Accordion (if available)
                if (subtasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceContainerHigh)
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isExpanded = !isExpanded }
                                    .testTag("subtasks_accordion_toggle_${task.id}"),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = Secondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${subtasks.size} Subtasks ready by Synapse",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Secondary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = "Expand",
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column(
                                    modifier = Modifier.padding(top = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    subtasks.forEach { subtask ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onToggleSubtask(subtask) }
                                                .testTag("subtask_item_${subtask.id}")
                                        ) {
                                            Checkbox(
                                                checked = subtask.isCompleted,
                                                onCheckedChange = { onToggleSubtask(subtask) },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = Primary,
                                                    uncheckedColor = OnSurfaceVariant,
                                                    checkmarkColor = OnPrimary
                                                ),
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = subtask.title,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = if (subtask.isCompleted) OnSurfaceVariant else OnSurface,
                                                    textDecoration = if (subtask.isCompleted) TextDecoration.LineThrough else null
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                text = "${subtask.estimatedMinutes}m",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = Primary,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Metadata Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Due time or location
                    if (task.isCompleted && !task.completedAtText.isNullOrBlank()) {
                        Text(
                            text = task.completedAtText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceVariant
                            )
                        )
                    } else if (!task.dueTimeText.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (task.dueTimeText.contains("Auto")) Icons.Default.CalendarMonth else Icons.Default.Alarm,
                                contentDescription = null,
                                tint = if (task.dueTimeText.contains("Due")) Error else Tertiary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = task.dueTimeText,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (task.dueTimeText.contains("Due")) Error else Tertiary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    } else if (!task.locationName.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Store,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = task.locationName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceVariant
                                )
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Right trailing label / button
                    if (!task.isCompleted && task.isAiPrioritized && task.linkUrl.isNullOrBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Synapse Prioritized",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Primary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    } else if (!task.linkUrl.isNullOrBlank()) {
                        Text(
                            text = "Open in GitHub",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Primary,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .clickable { onOpenLink?.invoke(task.linkUrl) }
                                .testTag("open_github_link")
                        )
                    } else if (!task.locationDistance.isNullOrBlank()) {
                        Text(
                            text = task.locationDistance,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}
