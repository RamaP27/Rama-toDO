package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, sortOrder ASC, id DESC")
    fun getAllTasksWithSubtasks(): Flow<List<TaskWithSubtasks>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskWithSubtasksById(taskId: Long): TaskWithSubtasks?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtask(subtask: SubTaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtasks(subtasks: List<SubTaskEntity>): List<Long>

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Update
    suspend fun updateSubtask(subtask: SubTaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    @Query("DELETE FROM subtasks WHERE id = :subtaskId")
    suspend fun deleteSubtaskById(subtaskId: Long)

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTaskCount(): Int

    @Query("DELETE FROM tasks")
    suspend fun clearAll()
}
