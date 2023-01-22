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

    private val from = MutableLiveData<Date?>()
    private val to = MutableLiveData<Date?>()
    val page = MutableLiveData<Int>()
    fun setPage(page: Int){
        this.page.value = page
    }

    private val refresh = MutableLiveData<Boolean>()
    fun init(from: Date?, to: Date?, page: Int){
        this.from.value = from
        this.to.value = to
        this.page.value = page
        this.refresh.value = true
    }

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

    /*================ LEGACY CODE ===========================*/

    val filteredLabel: MutableLiveData<List<Label>> by lazy {
        val result = MutableLiveData<List<Label>>()
        result.value = listOf()
        result
    }

    fun filter(list: List<Label>) {
        filteredLabel.postValue(list)
    }

    // first: complete, second: uncompleted
    val filteredStatus: MutableLiveData<Pair<Boolean, Boolean>> by lazy {
        val result = MutableLiveData<Pair<Boolean, Boolean>>()
        result.value = Pair(false, false)
        result
    }

    fun filter(complete: Boolean = false, uncomplete: Boolean = false) {
        filteredStatus.postValue(Pair(complete, uncomplete))
    }


    /*============================================================*/



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