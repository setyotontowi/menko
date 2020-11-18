package com.project.thisappistryingtomakeyoubetter.util

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.thisappistryingtomakeyoubetter.model.Task
import java.util.*

class TaskViewModel(application: Application, from: Date?, to: Date?)
    : ViewModel() {
    private val taskRepository: TaskRepository = TaskRepository(application, from, to)
    val tasks: LiveData<List<Task>>
    fun insert(task: Task?) {
        taskRepository.insert(task)
    }

    fun update(task: Task?) {
        taskRepository.update(task)
    }

    fun delete(task: Task?) {
        taskRepository.delete(task)
    }

    init {
        tasks = taskRepository.tasks!!
    }
}