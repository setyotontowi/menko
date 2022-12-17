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
        private val LIMIT = 5
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

    fun getHistoryAllTask(): LiveData<List<TaskWithLabel>?>{
        return taskDao.getHistoryAllTask()
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

    fun filterLabel(labels: List<Int>?, isFinish: Boolean? = null): LiveData<List<TaskWithLabel>?> {
        return if (!labels.isNullOrEmpty() && isFinish != null) {
                val number = if (isFinish) 1 else 0
                taskDao.filterList(labels, listOf(number))
            } else if (!labels.isNullOrEmpty() && isFinish == null) {
                taskDao.filterList(labels, listOf(0, 1))
            } else if (labels.isNullOrEmpty() && isFinish != null) {
                val number = if (isFinish) 1 else 0
                taskDao.filterList(listOf(number))
            } else {
                taskDao.getAllTaskWithLabel()
            }
    }

    fun getSummary(): Triple<Int, Int, Int>{
        val first = taskDao.countAllTask()
        val second = taskDao.countFinishedTask()
        val third = taskDao.countUnfinishedTask()

        return Triple(first, second, third)
    }

}