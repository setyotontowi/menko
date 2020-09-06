package com.project.thisappistryingtomakeyoubetter.util;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project.thisappistryingtomakeyoubetter.model.Task;

@Database(entities = {Task.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
