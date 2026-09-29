package com.example.data

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskRepository(private val taskDao: TaskDao) {

    val allTasksWithSubtasks: Flow<List<TaskWithSubtasks>> = taskDao.getAllTasksWithSubtasks()

    suspend fun seedInitialDataIfNeeded() {
        if (taskDao.getTaskCount() == 0) {
            seedDefaultData()
        }
    }

    suspend fun seedDefaultData() {
        taskDao.clearAll()

        // 1. Finalize Q4 AI Roadmap Deck
        val task1Id = taskDao.insertTask(
            TaskEntity(
                title = "Finalize Q4 AI Roadmap Deck",
                category = "Work",
                priority = "High Impact",
                isCompleted = false,
                estimatedTime = "45 mins",
                dueTimeText = "Due: Today, 11:30 AM",
                isAiPrioritized = true,
                sortOrder = 1
            )
        )
        taskDao.insertSubtasks(
            listOf(
                SubTaskEntity(taskId = task1Id, title = "Summarize OKR progress", estimatedMinutes = 15, isCompleted = false, sortOrder = 1),
                SubTaskEntity(taskId = task1Id, title = "Synthesize user feedback metrics", estimatedMinutes = 20, isCompleted = false, sortOrder = 2),
                SubTaskEntity(taskId = task1Id, title = "Export slides to PDF", estimatedMinutes = 10, isCompleted = false, sortOrder = 3)
            )
        )

        // 2. Review PR #412: Semantic Search
        val task2Id = taskDao.insertTask(
            TaskEntity(
                title = "Review PR #412: Semantic Search",
                category = "Dev",
                priority = "Medium",
                isCompleted = false,
                extraMeta = "14 files changed",
                dueTimeText = "Auto-scheduled: 2:00 PM",
                linkUrl = "https://github.com",
                isAiPrioritized = true,
                sortOrder = 2
            )
        )
        taskDao.insertSubtasks(
            listOf(
                SubTaskEntity(taskId = task2Id, title = "Inspect vector embedding benchmark results", estimatedMinutes = 15, isCompleted = false, sortOrder = 1),
                SubTaskEntity(taskId = task2Id, title = "Verify fallback to lexical search on low confidence", estimatedMinutes = 15, isCompleted = false, sortOrder = 2)
            )
        )

        // 3. Pick up running shoes from Downtown store
        taskDao.insertTask(
            TaskEntity(
                title = "Pick up running shoes from Downtown store",
                category = "Personal",
                priority = "Normal",
                isCompleted = false,
                locationNote = "Near office (after 5 PM)",
                locationName = "Fleet Feet Downtown",
                locationDistance = "0.8 mi away",
                isAiPrioritized = false,
                sortOrder = 3
            )
        )

        // 4. Completed: Daily sync with Product team
        taskDao.insertTask(
            TaskEntity(
                title = "Daily sync with Product team",
                category = "Work",
                priority = "Normal",
                isCompleted = true,
                completedAtText = "Done at 9:15 AM",
                isAiPrioritized = false,
                sortOrder = 4
            )
        )
    }

    suspend fun insertTaskWithSubtasks(
        title: String,
        category: String,
        priority: String,
        estimatedTime: String?,
        dueTimeText: String?,
        locationNote: String? = null,
        locationName: String? = null,
        locationDistance: String? = null,
        subtasks: List<Pair<String, Int>> = emptyList()
    ): Long {
        val taskId = taskDao.insertTask(
            TaskEntity(
                title = title,
                category = category,
                priority = priority,
                estimatedTime = estimatedTime,
                dueTimeText = dueTimeText,
                locationNote = locationNote,
                locationName = locationName,
                locationDistance = locationDistance,
                isAiPrioritized = true,
                isCompleted = false,
                sortOrder = 0
            )
        )

        if (subtasks.isNotEmpty()) {
            val subtaskEntities = subtasks.mapIndexed { index, (subTitle, mins) ->
                SubTaskEntity(
                    taskId = taskId,
                    title = subTitle,
                    estimatedMinutes = mins,
                    isCompleted = false,
                    sortOrder = index
                )
            }
            taskDao.insertSubtasks(subtaskEntities)
        }

        return taskId
    }

    suspend fun toggleTaskCompleted(task: TaskEntity) {
        val newStatus = !task.isCompleted
        val completedText = if (newStatus) {
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            "Done at " + sdf.format(Date())
        } else null

        taskDao.updateTask(
            task.copy(
                isCompleted = newStatus,
                completedAtText = completedText
            )
        )
    }

    suspend fun toggleSubtaskCompleted(subtask: SubTaskEntity) {
        taskDao.updateSubtask(subtask.copy(isCompleted = !subtask.isCompleted))
    }

    suspend fun addSubtask(taskId: Long, title: String, mins: Int = 15) {
        taskDao.insertSubtask(
            SubTaskEntity(
                taskId = taskId,
                title = title,
                estimatedMinutes = mins,
                isCompleted = false
            )
        )
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
}
