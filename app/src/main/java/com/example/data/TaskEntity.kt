package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: String, // "Work", "Dev", "Personal", "Research"
    val priority: String = "Normal", // "High Impact", "High Priority", "Medium", "Normal"
    val isCompleted: Boolean = false,
    val completedAtText: String? = null,
    val estimatedTime: String? = null, // "45 mins", "120 mins"
    val dueTimeText: String? = null, // "Due: Today, 11:30 AM", "Auto-scheduled: 2:00 PM"
    val locationNote: String? = null, // "Near office (after 5 PM)"
    val locationName: String? = null, // "Fleet Feet Downtown"
    val locationDistance: String? = null, // "0.8 mi away"
    val isAiPrioritized: Boolean = true,
    val extraMeta: String? = null, // "14 files changed", "Open in GitHub"
    val linkUrl: String? = null, // "https://github.com"
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
