package com.project.thisappistryingtomakeyoubetter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import com.project.thisappistryingtomakeyoubetter.util.LabelRepository
import com.project.thisappistryingtomakeyoubetter.util.TaskRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    val taskRepository: TaskRepository,
    val labelRepository: LabelRepository
): ViewModel() {

    val taskHistory: LiveData<List<TaskWithLabel>?> = taskRepository.getHistoryAllTask()
    val label: LiveData<List<Label>?> = labelRepository.getAll()
    var taskFilter = MutableLiveData<List<TaskWithLabel>?>()
    var filterLabel: List<Label>? = null
    var filterCompleted: Boolean? = null

    fun filterCompleted(completed: Boolean, unCompleted: Boolean) {
        filterCompleted = when {
            (completed && !unCompleted) -> true
            (!completed && unCompleted) -> false
            else -> null
        }
    }

    fun filter() {
        viewModelScope.launch(IO) {
            taskFilter.postValue(taskRepository.filterLabel(filterLabel, filterCompleted))
        }
    }

    val summary: LiveData<Triple<Int, Int, Int>> by lazy{
        val result = MutableLiveData<Triple<Int, Int, Int>>() // all, completed, uncompleted
        viewModelScope.launch(Dispatchers.IO) {
            result.postValue(taskRepository.getSummary())
        }
        result
    }

    fun insert(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { taskRepository.insert(task) }
    }

    fun update(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { taskRepository.update(task) }
    }

    fun delete(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { taskRepository.delete(task) }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) { taskRepository.deleteAll() }
    }

}