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
    @Query("SELECT * FROM task ORDER BY date DESC")
    fun getHistoryAllTask():LiveData<List<TaskWithLabel>?>

    @Transaction
    @Query("SELECT * FROM task ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getTaskWithLabelLimited(limit: Int, offset: Int): LiveData<List<TaskWithLabel>?>

    @Transaction
    @Query("SELECT distinct id, title, description, date, finish " +
            "FROM task JOIN labeling ON task.id == labeling.taskId " +
            "WHERE labelId IN (:labels) AND finish IN (:isFinished) ORDER BY DATE DESC")
    fun filterList(labels: List<Int>, isFinished: List<Int>): List<TaskWithLabel>?

    @Transaction
    @Query("SELECT * FROM task WHERE finish IN (:isFinished) ORDER BY DATE DESC")
    fun filterList(isFinished: List<Int>): List<TaskWithLabel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(task: Task): Long

    @Update
    fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as total FROM task")
    fun countAllTask(): Int

    @Query("SELECT COUNT(*) as totalFinished FROM task WHERE finish = 1 ")
    fun countFinishedTask(): Int

    @Query("SELECT COUNT(*) as totalFinished FROM task WHERE finish = 0 ")
    fun countUnfinishedTask(): Int
}