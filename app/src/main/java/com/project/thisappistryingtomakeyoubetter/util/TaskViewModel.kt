package com.project.thisappistryingtomakeyoubetter.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TaskViewModel @Inject constructor(
        private val taskRepository: TaskRepository,
        private val labelRepository: LabelRepository
) : ViewModel() {
    private val _tasks = MutableLiveData<List<Task>?>()
    val tasks: LiveData<List<Task>?> = _tasks

    private val _tasksWithLabel = MutableLiveData<List<TaskWithLabel>?>()
    val tasksWithLabel: LiveData<List<TaskWithLabel>?> = _tasksWithLabel

    private val _labels = MutableLiveData<List<Label>?>()
    val labels: LiveData<List<Label>?> = _labels

    private val TAG = "TaskViewModel"


    fun insert(task: Task) {
        viewModelScope.launch(IO) {
            taskRepository.insert(task)
        }
    }

    fun update(task: Task) {
        viewModelScope.launch(IO) { taskRepository.update(task) }
    }

    fun delete(task: Task) {
        viewModelScope.launch(IO) { taskRepository.delete(task) }
    }

    fun deleteAll(){
        viewModelScope.launch(IO) { taskRepository.deleteAll() }
    }

    @Suppress("UNUSED_PARAMETER")
    fun get(from: Date?, to: Date?): LiveData<List<Task>?> {
        viewModelScope.launch(IO) {
            val result = taskRepository.get(from, to)
            _tasks.postValue(result.value)
        }
        return taskRepository.get(from, to)
    }

    fun getTaskWithLabel(from: Date?, to: Date?): LiveData<List<TaskWithLabel>?>{
        return taskRepository.getTaskWithLabel(from, to)
    }

    fun getLabel(): LiveData<List<Label>?> {
        viewModelScope.launch(IO) {
            val result = labelRepository.getAll()
            _labels.postValue(result.value)
        }
        return labelRepository.getAll()
    }
}