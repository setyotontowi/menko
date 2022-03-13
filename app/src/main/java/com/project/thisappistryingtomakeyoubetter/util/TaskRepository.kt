package com.project.thisappistryingtomakeyoubetter.util

import android.util.Log
import androidx.lifecycle.LiveData
import com.project.thisappistryingtomakeyoubetter.model.Label
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

    companion object {
        private val LIMIT = 2
    }

    fun insert(task: Task){
        val id = taskDao.insertAll(task)
        for (label in task.labels){
            val labeling = Labeling(id.toInt(), label.id)
            labelingDao.insert(labeling)
        }
    }

    fun update(task: Task) {
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

    fun getTaskWithLabel(from: Date?, to: Date?, page: Int): LiveData<List<TaskWithLabel>?>{
        return if (from == null && to == null && page == -1) {
            taskDao.getAllTaskWithLabel()
        } else if (page == -1){
            taskDao.getTasksWithLabel(from, to)
        } else {
            taskDao.getTaskWithLabelLimited(LIMIT, LIMIT*page)
        }
    }

}