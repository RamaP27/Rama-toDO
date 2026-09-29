package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.SubTaskEntity
import com.example.data.TaskEntity
import com.example.data.TaskRepository
import com.example.data.TaskWithSubtasks
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val _energyLevel = MutableStateFlow(92)
    val energyLevel: StateFlow<Int> = _energyLevel.asStateFlow()

    private val _isSmartOrderActive = MutableStateFlow(true)
    val isSmartOrderActive: StateFlow<Boolean> = _isSmartOrderActive.asStateFlow()

    // Focus Mode session state
    private val _focusModeActive = MutableStateFlow(false)
    val focusModeActive: StateFlow<Boolean> = _focusModeActive.asStateFlow()

    private val _focusSecondsRemaining = MutableStateFlow(90 * 60)
    val focusSecondsRemaining: StateFlow<Int> = _focusSecondsRemaining.asStateFlow()

    private val _isFocusPaused = MutableStateFlow(false)
    val isFocusPaused: StateFlow<Boolean> = _isFocusPaused.asStateFlow()

    private var focusTimerJob: Job? = null

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TaskRepository(database.taskDao())
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    val tasksUiState: StateFlow<List<TaskWithSubtasks>> = combine(
        repository.allTasksWithSubtasks,
        _selectedFilter,
        _isSmartOrderActive
    ) { tasks, filter, isSmartOrder ->
        val filtered = when (filter) {
            "All" -> tasks
            "High Priority" -> tasks.filter { it.task.priority.contains("High", ignoreCase = true) }
            "Work" -> tasks.filter { it.task.category.equals("Work", ignoreCase = true) }
            "Personal" -> tasks.filter { it.task.category.equals("Personal", ignoreCase = true) }
            "AI Suggested" -> tasks.filter { it.task.isAiPrioritized }
            else -> tasks
        }

        if (isSmartOrder) {
            filtered.sortedWith(
                compareBy<TaskWithSubtasks> { it.task.isCompleted }
                    .thenByDescending { it.task.priority.contains("High", ignoreCase = true) }
                    .thenByDescending { it.task.isAiPrioritized }
                    .thenBy { it.task.sortOrder }
            )
        } else {
            filtered.sortedBy { it.task.isCompleted }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun selectFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun toggleSmartOrder() {
        _isSmartOrderActive.value = !_isSmartOrderActive.value
    }

    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskCompleted(task)
        }
    }

    fun toggleSubtask(subtask: SubTaskEntity) {
        viewModelScope.launch {
            repository.toggleSubtaskCompleted(subtask)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun updateEnergyLevel(level: Int) {
        _energyLevel.value = level.coerceIn(10, 100)
    }

    fun startFocusMode(minutes: Int = 90) {
        focusTimerJob?.cancel()
        _focusSecondsRemaining.value = minutes * 60
        _focusModeActive.value = true
        _isFocusPaused.value = false

        focusTimerJob = viewModelScope.launch {
            while (_focusSecondsRemaining.value > 0) {
                delay(1000L)
                if (!_isFocusPaused.value) {
                    _focusSecondsRemaining.value -= 1
                }
            }
            _focusModeActive.value = false
        }
    }

    fun toggleFocusPause() {
        _isFocusPaused.value = !_isFocusPaused.value
    }

    fun stopFocusMode() {
        focusTimerJob?.cancel()
        _focusModeActive.value = false
    }

    fun resetToDemoData() {
        viewModelScope.launch {
            repository.seedDefaultData()
        }
    }

    fun createQuickTask(
        title: String,
        category: String,
        priority: String,
        estimatedTime: String?,
        dueTimeText: String?,
        subtasks: List<Pair<String, Int>>
    ) {
        viewModelScope.launch {
            repository.insertTaskWithSubtasks(
                title = title,
                category = category,
                priority = priority,
                estimatedTime = estimatedTime,
                dueTimeText = dueTimeText,
                subtasks = subtasks
            )
        }
    }
}
