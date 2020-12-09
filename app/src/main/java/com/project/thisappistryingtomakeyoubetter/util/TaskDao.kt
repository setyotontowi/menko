package com.project.thisappistryingtomakeyoubetter.util

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.thisappistryingtomakeyoubetter.model.Task
import java.util.*
import javax.inject.Singleton

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE date BETWEEN :from AND :to")
    fun getTasks(from: Date?, to: Date?): LiveData<List<Task>?>

    @get:Query("SELECT * FROM task ORDER BY date DESC")
    val getAll:LiveData<List<Task>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg task: Task?)

    @Update
    suspend fun update(vararg task: Task?)

    @Delete
    suspend fun delete(vararg task: Task?)
}