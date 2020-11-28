package com.project.thisappistryingtomakeyoubetter.util;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.Date;
import java.util.List;

import javax.inject.Singleton;

@Singleton
@Dao
public interface TaskDao {
    @Query("SELECT * FROM task WHERE date BETWEEN :from AND :to")
    List<Task> getTasks(Date from, Date to);

    @Query("SELECT * FROM task WHERE date ORDER BY date DESC")
    List<Task> getAllTasks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Task... task);

    @Update
    void update(Task... task);

    @Delete
    void delete(Task... task);
}
