package com.project.thisappistryingtomakeyoubetter.util

import com.project.thisappistryingtomakeyoubetter.model.Task
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.collections.ArrayList

class TaskRepository @Inject constructor(
        private val taskDao: TaskDao
) {
    private val NUMBER_OF_THREADS = 4
    val databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    fun insert(task: Task?) = databaseWriterExecutor.execute {taskDao.insertAll(task)}


    fun update(task: Task?) = databaseWriterExecutor.execute {taskDao.update(task)}


    fun delete(task: Task?) = databaseWriterExecutor.execute {taskDao.delete(task)}

    fun get(from: Date?, to: Date?): List<Task>?{
        var list: List<Task> = ArrayList()
        databaseWriterExecutor.execute {
            if (from == null && to == null) {
                list = taskDao.allTasks
            } else {
                list = taskDao.getTasks(from, to)
            }
        }
        return list
    }

}