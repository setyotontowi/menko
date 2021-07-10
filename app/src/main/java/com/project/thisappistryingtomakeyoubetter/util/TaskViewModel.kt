package com.project.thisappistryingtomakeyoubetter.util

import android.util.Log
import androidx.lifecycle.*
import com.project.thisappistryingtomakeyoubetter.model.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val labelRepository: LabelRepository
) : ViewModel() {

    private val from = MutableLiveData<Date?>()
    fun setFrom(from: Date?) {
        this.from.value = from
    }

    private val to = MutableLiveData<Date?>()
    fun setTo(to: Date?) {
        this.to.value = to
    }


    val tasksWithLabel: LiveData<List<TaskWithLabel>?> = Transformations.switchMap(from) { from ->
        Transformations.switchMap(to) { to ->
                taskRepository.getTaskWithLabel(from, to)
        }
    }

    val taskGroup: LiveData<List<TaskGroup>> = Transformations.switchMap(tasksWithLabel){ list ->
        val result = MutableLiveData<List<TaskGroup>>()
        val taskGroup = mutableListOf<TaskGroup>()
        val map = mutableMapOf<Date, List<TaskWithLabel>>()

        list?.forEach {
            // using map, find the associate date then assign value with list
            val date = it.task.date?:Date()
            val a = map.get(date)
            if(a == null) {
                map[date] = listOf(it)
            } else {
                val b = a.toMutableList()
                b.add(it)
                map[date] = b
            }
        }

        map.forEach {
            taskGroup.add(TaskGroup(it.key, it.value))
        }
        result.value = taskGroup
        result
    }

    val label: LiveData<List<Label>?> = labelRepository.getAll()


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