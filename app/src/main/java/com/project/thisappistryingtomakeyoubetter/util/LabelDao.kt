package com.project.thisappistryingtomakeyoubetter.util

import androidx.lifecycle.LiveData
import androidx.room.*
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.LabelWithTask
import com.project.thisappistryingtomakeyoubetter.model.Task

@Dao
interface LabelDao {

    @get:Query("SELECT * FROM label")
    val getAll: LiveData<List<Label>?>

    @Transaction
    @Query("SELECT * FROM label")
    fun getLabelWithTask(): LiveData<List<LabelWithTask>?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg label: Label?)

    @Update
    suspend fun update(vararg label: Label?)

    @Delete
    suspend fun delete(vararg label: Label?)

    @Query("DELETE FROM label")
    suspend fun deleteAll()
}