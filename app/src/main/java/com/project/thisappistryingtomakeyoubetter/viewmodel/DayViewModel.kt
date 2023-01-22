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
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class DayViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val labelRepository: LabelRepository
) : ViewModel() {

    private val from = MutableLiveData<Date?>()
    private val to = MutableLiveData<Date?>()
    private val page = MutableLiveData<Int>()

    private val refresh = MutableLiveData<Boolean>()
    fun init(from: Date?, to: Date?, page: Int){
        this.from.value = from
        this.to.value = to
        this.page.value = page
        this.refresh.value = true
    }

    val tasksWithLabel: LiveData<List<TaskWithLabel>?> = switchMap(refresh){
        taskRepository.getTaskWithLabel(from.value, to.value, page.value?:0)
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