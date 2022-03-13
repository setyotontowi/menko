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
        this.updated.value = false
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
                    Transformations.switchMap(updated) { updated ->

                        val filteredStatusList = mutableListOf<TaskWithLabel>()
                        if(!updated) {
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
                        }

                        val result = MutableLiveData<List<TaskWithLabel>>()
                        result.value = filteredStatusList
                        result
                    }
                }
            }
        }
    }

    // using map, find the associate date then assign value with list
    private fun listToMap(list: List<TaskWithLabel>): MutableMap<Date, List<TaskWithLabel>>{
        val map = mutableMapOf<Date, List<TaskWithLabel>>()
        list.forEach {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date =
                    formatter.parse(formatter.format(it.task.date ?: Date())) ?: Date()
            val a = map[date]
            if (a == null) {
                map[date] = listOf(it)
            } else {
                val b = a.toMutableList()
                b.add(it)
                map[date] = b
            }
        }
        return map
    }

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

    val summary = Transformations.switchMap(activeTask) { list ->
        val result = MutableLiveData<Triple<Int, Int, Int>>() // all, completed, uncompleted

        val all = list?.size ?: 0
        val completed = list?.filter { it.task.isFinish }?.size ?: 0
        val uncompleted = list?.filter { !it.task.isFinish }?.size ?: 0

        result.value = Triple(all, completed, uncompleted)
        result
    }

    fun insert(task: Task) {
        viewModelScope.launch(IO) { taskRepository.insert(task) }
    }

    private val updated = MutableLiveData<Boolean>()
    fun update(task: Task) {
        updated.value = true
        viewModelScope.launch(IO) { taskRepository.update(task) }
    }

    fun delete(task: Task) {
        updated.value = true
        viewModelScope.launch(IO) { taskRepository.delete(task) }
    }

    fun deleteAll() {
        updated.value = false
        viewModelScope.launch(IO) { taskRepository.deleteAll() }
    }

}