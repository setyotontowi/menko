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

    private val activeTask = MutableLiveData<List<TaskWithLabel>?>()

    // to history
    private val fromToPage: LiveData<Triple<Date?, Date?, Int>> =
        Transformations.switchMap(from) { from ->
            Transformations.switchMap(to) { to ->
                Transformations.switchMap(page) { page ->
                    val result = MutableLiveData<Triple<Date?, Date?, Int>>()
                    result.value = Triple(from, to, page)
                    result
                }
            }
        }

    fun fetchList() {
        taskHistoryMutable.value = taskRepository.getHistoryAllTask().value
    }

    fun filterLabels(labels: List<Int>?, isFinish: Boolean?) {
        taskRepository.filterLabel(labels, isFinish)
    }

    private val taskHistoryMutable = MutableLiveData<List<TaskWithLabel>>()
    val taskHistory: LiveData<List<TaskWithLabel>> = taskHistoryMutable

    // TODO: do filtering in BE
    // Go with observing? Transformation switch map getTaskWithLabel(from, to, page)
    val taskGroup: LiveData<List<TaskWithLabel>> = Transformations.switchMap(fromToPage) { it ->
        Transformations.switchMap(taskRepository.getTaskWithLabel(it.first, it.second, it.third)) { list ->
            Transformations.switchMap(filteredLabel) { filteredLabel ->
                Transformations.switchMap(filteredStatus) { filteredStatus ->

                    val filteredStatusList = mutableListOf<TaskWithLabel>()
                    val filteredList = mutableListOf<TaskWithLabel>()
                    if (!filteredLabel.isNullOrEmpty()) {
                        for (label in filteredLabel) {
                            val tasks = list?.filter { it.labels.contains(label) }
                            filteredList.addAll(tasks ?: listOf())
                        }
                    } else {
                        filteredList.addAll(list ?: listOf())
                    }

                    if (filteredStatus.first != filteredStatus.second) {
                        if (filteredStatus.first) {
                            filteredStatusList.addAll(filteredList.filter { it.task.isFinish })
                        } else {
                            filteredStatusList.addAll(filteredList.filter { !it.task.isFinish })
                        }
                    } else {
                        filteredStatusList.addAll(filteredList)
                    }

                    filteredStatusList.sortByDescending { it.task.date }
                    activeTask.value = filteredStatusList

                    val result = MutableLiveData<List<TaskWithLabel>>()
                    result.value = filteredStatusList
                    result
                }
            }
        }
    }

    // to history?
    val label: LiveData<List<Label>?> = labelRepository.getAll()

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