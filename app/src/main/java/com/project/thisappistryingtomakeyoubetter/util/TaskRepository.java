package com.project.thisappistryingtomakeyoubetter.util;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.Date;
import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> tasks;

    TaskRepository(Application application, @Nullable Date from, @Nullable Date to) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();
        if(from == null && to == null){
            tasks = taskDao.getAllTasks();
        } else {
            tasks = taskDao.getTasks(from, to);
        }
    }

    LiveData<List<Task>> getTasks(){
        return tasks;
    }

    void insert(final Task task){
        AppDatabase.databaseWriterExecutor.execute(() -> taskDao.insertAll(task));

    }

    void update(final Task task){
        AppDatabase.databaseWriterExecutor.execute(() -> taskDao.update(task));
    }

    void delete(final Task task){
        AppDatabase.databaseWriterExecutor.execute(() -> taskDao.delete(task));
    }
}
