package com.project.thisappistryingtomakeyoubetter.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.project.thisappistryingtomakeyoubetter.model.Label;
import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

@Singleton
@Database(entities = {Task.class, Label.class}, version = 2)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract LabelDao labelDao();

    public static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `Label`" +
                    "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`name` TEXT)");
        }
    };
}
