package com.project.thisappistryingtomakeyoubetter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.project.thisappistryingtomakeyoubetter.model.*
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
    fun setPage(i: Int){
        Log.d("DEBUGGING", "setPage: $i")
        this.page.value = i
    }


    val tasksWithLabel: LiveData<List<TaskWithLabel>?> = Transformations.switchMap(from) { from ->
        Transformations.switchMap(to) { to ->
            Transformations.switchMap(page) { page ->
                taskRepository.getTaskWithLabel(from, to, page)
            }
        }
    }

    val taskGroup: LiveData<List<TaskGroup>> = Transformations.switchMap(tasksWithLabel){ list ->
        val result = MutableLiveData<List<TaskGroup>>()
        val taskGroup = mutableListOf<TaskGroup>()
        val map = mutableMapOf<Date, List<TaskWithLabel>>()

        list?.forEach {
            // using map, find the associate date then assign value with list
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = formatter.parse(formatter.format(it.task.date?:Date())) ?: Date()
            val a = map[date]
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