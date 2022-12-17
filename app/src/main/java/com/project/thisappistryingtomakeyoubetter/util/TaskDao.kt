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

    /*@Transaction
    @Query("SELECT * FROM task WHERE finish is :isComplete")
    fun filterStatus(isComplete: Boolean)*/

    /*@Transaction
    @Query("SELECT * FROM task WHERE ")*/

    @Transaction
    @Query("SELECT * FROM task ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getTaskWithLabelLimited(limit: Int, offset: Int): LiveData<List<TaskWithLabel>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(task: Task): Long

    @Transaction
    @Query("SELECT task.* " +
           "FROM task JOIN labeling ON task.id == labeling.taskId WHERE labelId IN (2)")
    fun filterList(): LiveData<List<TaskWithLabel>?>

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