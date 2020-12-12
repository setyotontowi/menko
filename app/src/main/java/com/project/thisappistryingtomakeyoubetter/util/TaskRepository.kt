package com.project.thisappistryingtomakeyoubetter.util

import android.util.Log
import androidx.lifecycle.LiveData
import com.project.thisappistryingtomakeyoubetter.model.Task
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class TaskRepository @Inject constructor(
        private val taskDao: TaskDao
) {

    suspend fun insert(task: Task?){
        taskDao.insertAll(task)
    }

    suspend fun update(task: Task?) = taskDao.update(task)

    suspend fun delete(task: Task?) = taskDao.delete(task)

    suspend fun deleteAll() = taskDao.deleteAll()

    fun get(from: Date?, to: Date?): LiveData<List<Task>?> {
        if (from == null && to == null) {
            return taskDao.getAll
        } else {
            return taskDao.getTasks(from, to)
        }
    }

}