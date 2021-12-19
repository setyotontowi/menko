package com.project.thisappistryingtomakeyoubetter.util

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import java.util.*
import javax.inject.Singleton

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE date BETWEEN :from AND :to")
    fun getTasks(from: Date?, to: Date?): LiveData<List<Task>?>

    @get:Query("SELECT * FROM task ORDER BY date DESC")
    val getAll:LiveData<List<Task>?>

    @Transaction
    @Query("SELECT * FROM task WHERE date BETWEEN :from AND :to")
    fun getTasksWithLabel(from: Date?, to:Date?): LiveData<List<TaskWithLabel>?>

    @Transaction
    @Query("SELECT * FROM task ORDER BY date DESC")
    fun getAllTaskWithLabel():LiveData<List<TaskWithLabel>?>

    @Transaction
    @Query("SELECT * FROM task ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getTaskWithLabelLimited(limit: Int, offset: Int): LiveData<List<TaskWithLabel>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(task: Task): Long

    @Update
    fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task")
    suspend fun deleteAll()
}