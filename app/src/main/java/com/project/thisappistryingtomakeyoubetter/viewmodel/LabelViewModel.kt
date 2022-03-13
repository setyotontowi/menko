package com.project.thisappistryingtomakeyoubetter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.LabelWithTask
import com.project.thisappistryingtomakeyoubetter.util.LabelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LabelViewModel @Inject constructor(
        private val labelRepository: LabelRepository
        ) : ViewModel() {
    private val _labels = MutableLiveData<List<Label>?>()
    val labels: LiveData<List<Label>?> = _labels

    private val TAG = "LabelViewModel"

    fun insert(label: Label?) {
        viewModelScope.launch(Dispatchers.IO) {
            labelRepository.insert(label)
        }
    }

    fun update(label: Label?) {
        viewModelScope.launch(Dispatchers.IO) { labelRepository.update(label) }
    }

    fun delete(label: Label?) {
        viewModelScope.launch(Dispatchers.IO) { labelRepository.delete(label) }
    }

    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) { labelRepository.deleteAll() }
    }

    @Suppress("UNUSED_PARAMETER")
    fun get(): LiveData<List<Label>?> {
        viewModelScope.launch(Dispatchers.IO) {
            val result = labelRepository.getAll()
            _labels.postValue(result.value)
        }
        return labelRepository.getAll()
    }

    fun getLabelWithTask(): LiveData<List<LabelWithTask>?> { return labelRepository.getLabelWithTasks()}

    enum class State(var value: Int) {
        LOADING(0), SUCCESS(1), FAILED(-1);
    }

}