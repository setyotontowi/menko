package com.project.thisappistryingtomakeyoubetter.util

import androidx.lifecycle.LiveData
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.LabelWithTask
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LabelRepository @Inject constructor(private val labelDao: LabelDao) {

    suspend fun insert(label: Label?) {
        labelDao.insertAll(label)
    }

    suspend fun update(label: Label?) = labelDao.update(label)

    suspend fun delete(label: Label?) = labelDao.delete(label)

    suspend fun deleteAll() = labelDao.deleteAll()

    fun getAll(): LiveData<List<Label>?> { return labelDao.getAll }

    fun getLabelWithTasks(): LiveData<List<LabelWithTask>?> {return labelDao.getLabelWithTask()}
}