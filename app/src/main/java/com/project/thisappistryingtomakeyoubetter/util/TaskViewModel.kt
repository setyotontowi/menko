package com.project.thisappistryingtomakeyoubetter.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.thisappistryingtomakeyoubetter.model.Task
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

class TaskViewModel @Inject constructor(
        private val taskRepository: TaskRepository
) : ViewModel() {
    private val _tasks = MutableLiveData<List<Task>?>()
    val tasks: LiveData<List<Task>?> = _tasks

    private val TAG = "TaskViewModel"


    fun insert(task: Task?) {
        viewModelScope.launch(IO) {
            taskRepository.insert(task)
        }
    }

    fun update(task: Task?) {
        viewModelScope.launch(IO) { taskRepository.update(task) }
    }

    fun delete(task: Task?) {
        viewModelScope.launch(IO) { taskRepository.delete(task) }
    }

    @Suppress("UNUSED_PARAMETER")
    fun get(from: Date?, to: Date?): LiveData<List<Task>?> {
        viewModelScope.launch(IO) {
            val result = taskRepository.get(from, to)
            _tasks.postValue(result.value)
        }
        val result = taskRepository.get(from, to)
        return result
    }
}