package com.project.thisappistryingtomakeyoubetter.util

import android.app.Application
import androidx.lifecycle.LiveData
import com.project.thisappistryingtomakeyoubetter.model.Task
import java.util.*

class TaskRepository(application: Application?, from: Date?, to: Date?) {
    private val taskDao: TaskDao
    var tasks: LiveData<List<Task>>? = null
    fun insert(task: Task?) {
        AppDatabase.databaseWriterExecutor.execute { taskDao.insertAll(task) }
    }

    fun update(task: Task?) {
        AppDatabase.databaseWriterExecutor.execute { taskDao.update(task) }
    }

    fun delete(task: Task?) {
        AppDatabase.databaseWriterExecutor.execute { taskDao.delete(task) }
    }

    init {
        val db = AppDatabase.getInstance(application)
        taskDao = db.taskDao()
        tasks = if (from == null && to == null) {
            taskDao.allTasks
        } else {
            taskDao.getTasks(from, to)
        }
    }
}