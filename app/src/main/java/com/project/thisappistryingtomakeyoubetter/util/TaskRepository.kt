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
    private val TAG = "TaskRepository"
    suspend fun insert(task: Task){
        val id = taskDao.insertAll(task)
        // TODO: 20/12/2020 insert a task with returning and id and then insert to table task-label for each label
        for (label in task.labels){
            Log.d(TAG, "insert: ${label.name}")
        }
    }

    suspend fun update(task: Task) = taskDao.update(task)

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun deleteAll() = taskDao.deleteAll()

    fun get(from: Date?, to: Date?): LiveData<List<Task>?> {
        return if (from == null && to == null) {
            taskDao.getAll
        } else {
            taskDao.getTasks(from, to)
        }
    }

}