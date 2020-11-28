package com.project.thisappistryingtomakeyoubetter.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

@Singleton
@Database(entities = {Task.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

}
