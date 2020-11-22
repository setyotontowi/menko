package com.project.thisappistryingtomakeyoubetter.util

import android.app.Application
import androidx.lifecycle.LiveData
import com.project.thisappistryingtomakeyoubetter.model.Task
import java.util.*

class TaskRepository(val application: Application?) {
    private val taskDao: TaskDao
    var tasks: List<Task>? = null
    fun insert(task: Task?) {
        AppDatabase.databaseWriterExecutor.execute { taskDao.insertAll(task) }
    }

    fun update(task: Task?) {
        AppDatabase.databaseWriterExecutor.execute { taskDao.update(task) }
    }

    fun delete(task: Task?) {
        AppDatabase.databaseWriterExecutor.execute { taskDao.delete(task) }
    }

    fun get(from: Date?, to: Date?){
        tasks = if (from == null && to == null) {
            taskDao.allTasks
        } else {
            taskDao.getTasks(from, to)
        }
    }

    init {
        val db = AppDatabase.getInstance(application)
        taskDao = db.taskDao()
        get(null, null)
    }

}