package com.project.thisappistryingtomakeyoubetter.util

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.thisappistryingtomakeyoubetter.model.Labeling

@Dao
interface LabelingDao {

    @Query("SELECT * FROM Labeling WHERE task = :idTask")
    fun getEachLabelInTask(idTask: Int): LiveData<List<Labeling>>

    @Query("SELECT * FROM Labeling WHERE label = :idLabel")
    fun getEachTaskInLabel(idLabel: Int): LiveData<List<Labeling>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(labeling: Labeling)

    @Delete
    suspend fun delete(labeling: Labeling)

}