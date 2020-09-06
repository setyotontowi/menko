package com.project.thisappistryingtomakeyoubetter.util;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task WHERE date BETWEEN :from AND :to")
    List<Task> getAll(Date from, Date to);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Task... task);

    @Update
    void update(Task... task);

    @Delete
    void delete(Task... task);
}
