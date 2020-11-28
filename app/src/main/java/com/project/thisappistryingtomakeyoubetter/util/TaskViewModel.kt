package com.project.thisappistryingtomakeyoubetter.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.thisappistryingtomakeyoubetter.model.Task
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named

class TaskViewModel @Inject constructor(
        private val taskRepository: TaskRepository
) : ViewModel() {
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val TAG = "TaskViewModel"
    private val NUMBER_OF_THREADS = 4
    val databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

    init {
        Log.d(TAG, "Init: Start")
    }

    fun insert(task: Task?) {
        databaseWriterExecutor.execute { taskRepository.insert(task) }
    }

    fun update(task: Task?) {
        databaseWriterExecutor.execute { taskRepository.update(task) }
    }

    fun delete(task: Task?) {
        databaseWriterExecutor.execute { taskRepository.delete(task) }
    }

    @Suppress("UNUSED_PARAMETER")
    fun get(from: Date?, to: Date?) {
        databaseWriterExecutor.execute {
            Log.d(TAG, "get: $from")
            val result = taskRepository.get(from, to)
            _tasks.postValue(result)
        }
    }
}