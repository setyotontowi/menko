package com.project.thisappistryingtomakeyoubetter.util

import android.util.Log
import androidx.lifecycle.LiveData
import com.project.thisappistryingtomakeyoubetter.model.Labeling
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
        private val taskDao: TaskDao,
        private val labelingDao: LabelingDao
) {
    private val TAG = "TaskRepository"
    suspend fun insert(task: Task){
        val id = taskDao.insertAll(task)
        Log.d(TAG, "insert: Observer Inserting Task ${task.title}")
        for (label in task.labels){
            val labeling = Labeling(id.toInt(), label.id)
            labelingDao.insert(labeling)
            Log.d(TAG, "insert: Observer Inserting Label ${label.name}")
        }
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
        labelingDao.deleteLabelInTask(task.id)
        for(label in task.labels){
            val labeling = Labeling(task.id, label.id)
            labelingDao.insert(labeling)
        }
    }

    suspend fun delete(task: Task) = taskDao.delete(task)

    suspend fun deleteAll() {
        taskDao.deleteAll()
        labelingDao.deleteAll()
    }

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