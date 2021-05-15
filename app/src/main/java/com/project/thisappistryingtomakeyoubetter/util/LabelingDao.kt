package com.project.thisappistryingtomakeyoubetter.util

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.thisappistryingtomakeyoubetter.model.Labeling

@Dao
interface LabelingDao {

    @Query("SELECT * FROM Labeling")
    fun getAllLabeling(): LiveData<List<Labeling>>

    @Query("SELECT * FROM Labeling WHERE taskId = :idTask")
    fun getEachLabelInTask(idTask: Int): LiveData<List<Labeling>>

    @Query("SELECT * FROM Labeling WHERE labelId = :idLabel")
    fun getEachTaskInLabel(idLabel: Int): LiveData<List<Labeling>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(labeling: Labeling)

    @Delete
    suspend fun delete(labeling: Labeling)

    @Query("DELETE FROM Labeling WHERE taskId = :taskId")
    fun deleteLabelInTask(taskId: Int)

    @Query("DELETE FROM Labeling")
    suspend fun deleteAll()

}