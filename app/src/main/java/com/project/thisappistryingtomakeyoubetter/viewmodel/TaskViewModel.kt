package com.project.thisappistryingtomakeyoubetter.viewmodel

import androidx.lifecycle.*
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskGroup
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import com.project.thisappistryingtomakeyoubetter.util.LabelRepository
import com.project.thisappistryingtomakeyoubetter.util.TaskRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    labelRepository: LabelRepository
) : ViewModel() {

    private val from = MutableLiveData<Date?>()
    fun setFrom(from: Date?) {
        this.from.value = from
    }

    private val to = MutableLiveData<Date?>()
    fun setTo(to: Date?) {
        this.to.value = to
    }

    val page = MutableLiveData<Int>()
    fun setPage(i: Int) {
        this.page.value = i
    }


    /* taskWithLabel is a LiveData, return from getTaskWithLabel is also a live data.
    *  So it will trigger any change in room and straight observing it */
    val tasksWithLabel: LiveData<List<TaskWithLabel>?> = Transformations.switchMap(from) { from ->
        Transformations.switchMap(to) { to ->
            Transformations.switchMap(page) { page ->
                taskRepository.getTaskWithLabel(from, to, page)
            }
        }
    }

    private val activeTask = MutableLiveData<List<TaskWithLabel>?>()

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

    // Go with observing? Transformation switch map getTaskWithLabel(from, to, page)
    val taskGroup: LiveData<List<TaskWithLabel>> = Transformations.switchMap(fromToPage) { it ->
        Transformations.switchMap(taskRepository.getTaskWithLabel(it.first, it.second, it.third)) { list ->
            Transformations.switchMap(filteredLabel) { filteredLabel ->
                Transformations.switchMap(filteredStatus) { filteredStatus ->

                        val filteredList = list
                            .filterByLabel(filteredLabel)
                            .filterByStatus(filteredStatus)
                            .sortedByDescending { it.task.date }

                        activeTask.value = filteredList

                        val result = MutableLiveData<List<TaskWithLabel>>()
                        result.value = filteredList
                        result
                }
            }
        }
    }

    val taskHistory: LiveData<List<TaskWithLabel>> = Transformations.switchMap(taskRepository.getTaskWithLabel(null, null, -1)) { list ->
        Transformations.switchMap(filteredLabel) { filteredLabel ->
            Transformations.switchMap(filteredStatus) { filteredStatus ->
                val result = MutableLiveData<List<TaskWithLabel>>()
                val filteredList = list
                    .filterByLabel(filteredLabel)
                    .filterByStatus(filteredStatus)
                    .sortedByDescending { it.task.date }
                result.value = filteredList
                result
            }
        }
    }

    val label: LiveData<List<Label>?> = labelRepository.getAll()

    private fun List<TaskWithLabel>?.filterByLabel(filteredLabel: List<Label>?): List<TaskWithLabel>{
        val filteredList = mutableListOf<TaskWithLabel>()
        val list = this
        if (!filteredLabel.isNullOrEmpty()) {
            for (label in filteredLabel) {
                val tasks = list?.filter { it.labels.contains(label) }
                filteredList.addAll(tasks ?: listOf())
            }
        } else {
            filteredList.addAll(list ?: listOf())
        }

        return filteredList
    }

    private fun List<TaskWithLabel>.filterByStatus(filteredStatus: Pair<Boolean, Boolean>): List<TaskWithLabel>{
        val filteredStatusList = mutableListOf<TaskWithLabel>()
        val filteredList = this
        if (filteredStatus.first != filteredStatus.second) {
            if (filteredStatus.first) {
                filteredStatusList.addAll(filteredList.filter { it.task.isFinish })
            } else {
                filteredStatusList.addAll(filteredList.filter { !it.task.isFinish })
            }
        } else {
            filteredStatusList.addAll(filteredList)
        }

        return filteredStatusList
    }

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
        viewModelScope.launch(IO) {
            result.postValue(taskRepository.getSummary())
        }
        result
    }

    fun insert(task: Task) {
        viewModelScope.launch(IO) { taskRepository.insert(task) }
    }

    fun update(task: Task) {
        viewModelScope.launch(IO) { taskRepository.update(task) }
    }

    fun delete(task: Task) {
        viewModelScope.launch(IO) { taskRepository.delete(task) }
    }

    fun deleteAll() {
        viewModelScope.launch(IO) { taskRepository.deleteAll() }
    }

}