package com.project.thisappistryingtomakeyoubetter.util

import android.util.Log
import androidx.lifecycle.LiveData
import com.project.thisappistryingtomakeyoubetter.model.Labeling
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class TaskRepository @Inject constructor(
        private val taskDao: TaskDao,
        private val labelingDao: LabelingDao
) {
    private val TAG = "TaskRepository"
    suspend fun insert(task: Task){
        val id = taskDao.insertAll(task)
        for (label in task.labels){
            Log.d(TAG, "insert: ${label.name}")
            val labeling = Labeling(id.toInt(), label.id)
            labelingDao.insert(labeling)
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

    fun getTaskWithLabel(from: Date?, to: Date?): LiveData<List<TaskWithLabel>?>{
        return if (from == null && to == null) {
            taskDao.getAllTaskWithLabel()
        } else {
            taskDao.getTasksWithLabel(from, to)
        }
    }

}