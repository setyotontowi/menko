package com.project.thisappistryingtomakeyoubetter.util

import com.project.thisappistryingtomakeyoubetter.model.Task
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.collections.ArrayList

class TaskRepository @Inject constructor(
        private val taskDao: TaskDao
) {

    fun insert(task: Task?) = taskDao.insertAll(task)


    fun update(task: Task?) = taskDao.update(task)


    fun delete(task: Task?) = taskDao.delete(task)

    fun get(from: Date?, to: Date?): List<Task>? {
        val list: List<Task>
        if (from == null && to == null) {
            list = taskDao.allTasks
        } else {
            list = taskDao.getTasks(from, to)
        }
        return list
    }

}