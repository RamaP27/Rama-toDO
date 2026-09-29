package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ProfileDialog
import com.example.ui.components.SynapseTopAppBar
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.InversePrimary
import com.example.ui.theme.OnErrorContainer
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Outline
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceBright
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryContainer
import com.example.ui.viewmodel.CopilotViewModel
import kotlinx.coroutines.launch

@Composable
fun CopilotSessionScreen(
    viewModel: CopilotViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onNavigateBack)

    val promptText by viewModel.promptInput.collectAsStateWithLifecycle()
    val isListening by viewModel.isListening.collectAsStateWithLifecycle()
    val isAnalyzing by viewModel.isAnalyzing.collectAsStateWithLifecycle()
    val attachedFile by viewModel.attachedFile.collectAsStateWithLifecycle()
    val selectedIntegrations by viewModel.selectedIntegrations.collectAsStateWithLifecycle()
    val synthesis by viewModel.synthesis.collectAsStateWithLifecycle()
    val isDeploying by viewModel.isDeploying.collectAsStateWithLifecycle()
    val deploySuccess by viewModel.deploySuccess.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showProfileDialog by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_mic"
    )

    Scaffold(
        topBar = {
            SynapseTopAppBar(
                title = "Ai Copilot Session",
                subtitle = null,
                isCopilot = true,
                onBackClick = onNavigateBack,
                onProfileClick = { showProfileDialog = true },
                onOptionsClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Cognitive Engine v2.4 initialized")
                    }
                }
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
            contentPadding = PaddingValues(top = 10.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // NLP Engine Status Row
            item {
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
                                .size(8.dp)
                                .alpha(pulseAlpha)
                                .background(Tertiary, CircleShape)
                        )
                        Text(
                            text = "SYNAPSE NLP ACTIVE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Tertiary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
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
                            text = "v2.4 Cognitive Engine",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceVariant
                            )
                        )
                    }
                }
            }

            // Natural Language Intent Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainer)
                        .padding(16.dp)
                ) {
                    // Glow effect
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .offset(x = 200.dp, y = (-20).dp)
                            .blur(40.dp)
                            .background(Primary.copy(alpha = 0.12f), CircleShape)
                    )

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Natural Language Intent",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceVariant
                                )
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (isAnalyzing) "Parsing intent..." else "Parsed in ${synthesis.parseTimeMs}ms",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Primary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Text Input
                        BasicTextField(
                            value = promptText,
                            onValueChange = { viewModel.onPromptChange(it) },
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                color = OnSurface,
                                lineHeight = 24.sp
                            ),
                            cursorBrush = SolidColor(Primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .testTag("ai_task_input"),
                            decorationBox = { innerTextField ->
                                if (promptText.isEmpty()) {
                                    Text(
                                        text = "Tell Synapse what to do...",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Outline
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        )

                        // Attachment preview if attached
                        if (attachedFile != null) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceContainerHighest)
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachFile,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = attachedFile ?: "",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Primary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Intent Action Controls
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Listening toggle
                                Row(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(if (isListening) SecondaryContainer else SurfaceContainerHigh)
                                        .clickable { viewModel.toggleListening() }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                        .testTag("mic_toggle_btn"),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (isListening) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .alpha(pulseAlpha)
                                                .background(Tertiary, CircleShape)
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "Microphone",
                                        tint = if (isListening) OnSecondaryContainer else OnSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isListening) "Listening" else "Voice Input",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = if (isListening) OnSecondaryContainer else OnSurfaceVariant,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }

                                // Attach file button
                                Row(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(SurfaceContainerHigh)
                                        .clickable { viewModel.toggleAttachment() }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .testTag("attach_file_button"),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = "Attach",
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Attach",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = OnSurfaceVariant
                                        )
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { viewModel.clearPrompt() },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("clear_prompt_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Backspace,
                                        contentDescription = "Clear",
                                        tint = Outline,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.reAnalyze() },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("reanalyze_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Re-analyze",
                                        tint = Primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Real-Time Cognitive Breakdown Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Insights,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Real-Time Cognitive Breakdown",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                        )
                    }

                    Text(
                        text = "Confidence: ${synthesis.confidence}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Tertiary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Synthesized Title & Metadata Card
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
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "SYNTHESIZED TITLE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = OnSurfaceVariant,
                                        letterSpacing = 1.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = synthesis.title,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurface
                                    )
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryContainer.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Slideshow,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Chips: Due date, priority, focus block
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(SurfaceContainer)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = Tertiary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = synthesis.dueText,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = OnSurface,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(ErrorContainer)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PriorityHigh,
                                    contentDescription = null,
                                    tint = OnErrorContainer,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = synthesis.priority,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = OnErrorContainer,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(SurfaceContainer)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = synthesis.focusBlockText,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Secondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // GCal conflict notice
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHighest)
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = synthesis.gcalNote,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = OnSurfaceVariant
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Deconstructed Subtasks
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Checklist,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Deconstructed Subtasks",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface
                                )
                            )
                        }

                        Text(
                            text = "${synthesis.subtasks.size} segments • 2h est.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        synthesis.subtasks.forEach { subtask ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SurfaceContainer)
                                    .clickable { viewModel.toggleSubtask(subtask.id) }
                                    .padding(horizontal = 12.dp, vertical = 12.dp)
                                    .testTag("copilot_subtask_${subtask.id}"),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                                                tint = OnPrimaryContainer,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = subtask.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = if (subtask.isCompleted) OnSurfaceVariant else OnSurface,
                                            textDecoration = if (subtask.isCompleted) TextDecoration.LineThrough else null,
                                            fontWeight = if (subtask.isCompleted) FontWeight.Normal else FontWeight.Medium
                                        ),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Text(
                                    text = subtask.durationText,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Primary,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Suggested Co-Pilot Integrations
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "SUGGESTED CO-PILOT INTEGRATIONS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = OnSurfaceVariant,
                                letterSpacing = 1.sp
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val integrations = listOf(
                            Triple("Add Sarah as reviewer", Icons.Default.PersonAdd, Secondary),
                            Triple("Hold slot in Google Calendar", Icons.Default.EventSeat, Tertiary),
                            Triple("Link Figma Workspace", Icons.Default.Link, Primary)
                        )

                        integrations.forEach { (name, icon, tint) ->
                            val isSelected = selectedIntegrations.contains(name)
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) SurfaceBright else SurfaceContainerHigh
                                    )
                                    .clickable { viewModel.toggleIntegration(name) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .testTag("integration_chip_${name.replace(" ", "_")}"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = tint,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = OnSurface,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Create Task Button with AI Breakdown
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (deploySuccess) {
                                    Brush.horizontalGradient(listOf(TertiaryContainer, Tertiary))
                                } else {
                                    Brush.horizontalGradient(
                                        listOf(PrimaryContainer, SecondaryContainer, Primary)
                                    )
                                }
                            )
                            .clickable(enabled = !isDeploying) {
                                viewModel.deployTaskWithBreakdown(
                                    onSuccess = {
                                        onNavigateBack()
                                    }
                                )
                            }
                            .padding(vertical = 15.dp)
                            .testTag("create_task_ai_breakdown_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (isDeploying) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = OnPrimary,
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    text = "Scheduling & Syncing...",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = OnPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            } else if (deploySuccess) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF003640),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Task Deployed to Backlog!",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color(0xFF003640),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AddTask,
                                    contentDescription = null,
                                    tint = OnPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Create Task with AI Breakdown",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = OnPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }

                    // Secondary actions: Save as Draft + Tune button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Draft saved to Synapse local cache")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceContainer,
                                contentColor = OnSurfaceVariant
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("save_draft_btn")
                        ) {
                            Text(
                                text = "Save as Draft",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(SurfaceContainer)
                                .clickable {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Model parameters: Temp 0.3, Top-P 0.95")
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Customize Parameters",
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showProfileDialog) {
        ProfileDialog(
            onResetDemoData = {},
            onDismiss = { showProfileDialog = false }
        )
    }
}
