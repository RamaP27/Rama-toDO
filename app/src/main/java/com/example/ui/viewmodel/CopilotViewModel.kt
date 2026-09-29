package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.TaskRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CopilotSubtask(
    val id: String,
    val title: String,
    val durationText: String,
    val durationMinutes: Int,
    val isCompleted: Boolean = false
)

data class CopilotSynthesis(
    val title: String,
    val dueText: String,
    val priority: String,
    val focusBlockText: String,
    val gcalNote: String,
    val subtasks: List<CopilotSubtask>,
    val confidence: Int = 98,
    val parseTimeMs: Int = 140
)

class CopilotViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository

    private val _promptInput = MutableStateFlow(
        "Prepare presentation for executive team about Q4 AI roadmap by Friday 2pm, prioritize high and break into steps"
    )
    val promptInput: StateFlow<String> = _promptInput.asStateFlow()

    private val _isListening = MutableStateFlow(true)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _attachedFile = MutableStateFlow<String?>(null)
    val attachedFile: StateFlow<String?> = _attachedFile.asStateFlow()

    // Integrations toggled
    private val _selectedIntegrations = MutableStateFlow(setOf("Add Sarah as reviewer", "Hold slot in Google Calendar"))
    val selectedIntegrations: StateFlow<Set<String>> = _selectedIntegrations.asStateFlow()

    private val _synthesis = MutableStateFlow(
        CopilotSynthesis(
            title = "Executive Q4 AI Roadmap Presentation",
            dueText = "Fri, Oct 27 • 2:00 PM",
            priority = "High Priority",
            focusBlockText = "Deep Focus (120m)",
            gcalNote = "GCal conflict averted: Free slot detected 1:30 PM - 3:30 PM.",
            subtasks = listOf(
                CopilotSubtask("1", "Gather metric highlights from product analytics", "30m", 30, isCompleted = true),
                CopilotSubtask("2", "Draft slide outline & core narrative arc", "45m", 45, isCompleted = false),
                CopilotSubtask("3", "Design visual diagrams & executive summary slide", "30m", 30, isCompleted = false),
                CopilotSubtask("4", "Dry run presentation & rehearse timing", "15m", 15, isCompleted = false)
            ),
            confidence = 98,
            parseTimeMs = 140
        )
    )
    val synthesis: StateFlow<CopilotSynthesis> = _synthesis.asStateFlow()

    private val _isDeploying = MutableStateFlow(false)
    val isDeploying: StateFlow<Boolean> = _isDeploying.asStateFlow()

    private val _deploySuccess = MutableStateFlow(false)
    val deploySuccess: StateFlow<Boolean> = _deploySuccess.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TaskRepository(database.taskDao())
    }

    fun onPromptChange(newText: String) {
        _promptInput.value = newText
        analyzePrompt(newText)
    }

    fun toggleListening() {
        _isListening.value = !_isListening.value
    }

    fun clearPrompt() {
        _promptInput.value = ""
    }

    fun toggleAttachment() {
        _attachedFile.value = if (_attachedFile.value == null) "q3_metrics_summary.pdf" else null
    }

    fun toggleIntegration(name: String) {
        val current = _selectedIntegrations.value.toMutableSet()
        if (current.contains(name)) current.remove(name) else current.add(name)
        _selectedIntegrations.value = current
    }

    fun toggleSubtask(id: String) {
        val current = _synthesis.value
        val updated = current.subtasks.map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
        }
        _synthesis.value = current.copy(subtasks = updated)
    }

    fun reAnalyze() {
        analyzePrompt(_promptInput.value, force = true)
    }

    private fun analyzePrompt(text: String, force: Boolean = false) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            delay(180L)

            val lower = text.lowercase()
            val synthesizedTitle = when {
                lower.contains("presentation") || lower.contains("roadmap") -> "Executive Q4 AI Roadmap Presentation"
                lower.contains("pr") || lower.contains("code") || lower.contains("search") -> "Review Semantic Search & Indexing Engine"
                lower.contains("interview") || lower.contains("hiring") -> "Conduct Technical Staff Architecture Interview"
                lower.contains("shoes") || lower.contains("personal") || lower.contains("store") -> "Pick up running shoes from Downtown store"
                text.isNotBlank() -> text.split(",").firstOrNull()?.trim()?.capitalizeWords() ?: "New Synthesized Task"
                else -> "Draft Project Objective"
            }

            val priority = if (lower.contains("high") || lower.contains("urgent") || lower.contains("exec")) "High Priority" else "Medium"
            val due = if (lower.contains("friday") || lower.contains("2pm")) "Fri, Oct 27 • 2:00 PM" else "Today, 5:00 PM"
            val deepFocus = if (lower.contains("presentation") || lower.contains("break")) "Deep Focus (120m)" else "Focus Block (45m)"

            val subtasks = if (lower.contains("presentation") || lower.contains("roadmap")) {
                listOf(
                    CopilotSubtask("1", "Gather metric highlights from product analytics", "30m", 30, isCompleted = true),
                    CopilotSubtask("2", "Draft slide outline & core narrative arc", "45m", 45, isCompleted = false),
                    CopilotSubtask("3", "Design visual diagrams & executive summary slide", "30m", 30, isCompleted = false),
                    CopilotSubtask("4", "Dry run presentation & rehearse timing", "15m", 15, isCompleted = false)
                )
            } else {
                listOf(
                    CopilotSubtask("1", "Define core deliverables and scope", "20m", 20, isCompleted = false),
                    CopilotSubtask("2", "Draft initial proposal and share with team", "40m", 40, isCompleted = false),
                    CopilotSubtask("3", "Final verification and sign-off", "15m", 15, isCompleted = false)
                )
            }

            _synthesis.value = CopilotSynthesis(
                title = synthesizedTitle,
                dueText = due,
                priority = priority,
                focusBlockText = deepFocus,
                gcalNote = "GCal conflict averted: Free slot detected 1:30 PM - 3:30 PM.",
                subtasks = subtasks,
                confidence = 98,
                parseTimeMs = (120..150).random()
            )
            _isAnalyzing.value = false
        }
    }

    fun deployTaskWithBreakdown(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isDeploying.value = true
            delay(750L) // Visual deployment feedback

            val current = _synthesis.value
            val category = if (current.priority.contains("High")) "Work" else "Dev"
            val subtaskList = current.subtasks.map { it.title to it.durationMinutes }

            repository.insertTaskWithSubtasks(
                title = current.title,
                category = category,
                priority = current.priority,
                estimatedTime = current.focusBlockText.replace("Deep Focus ", "").replace("(", "").replace(")", ""),
                dueTimeText = "Due: " + current.dueText,
                subtasks = subtaskList
            )

            _isDeploying.value = false
            _deploySuccess.value = true
            delay(300L)
            onSuccess()
        }
    }

    private fun String.capitalizeWords(): String {
        return split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}
