package com.project.thisappistryingtomakeyoubetter.util;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.Date;
import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTask;

    TaskRepository(Application application, Date from, Date to) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();
        allTask = taskDao.getTasks(from, to);
    }

    LiveData<List<Task>> getTasks(){
        return allTask;
    }

    void insert(final Task task){
        AppDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.insertAll(task);
            }
        });

    }

    void update(final Task task){
        AppDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.update(task);
            }
        });
    }

    void delete(final Task task){
        AppDatabase.databaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.delete(task);
            }
        });
    }
}
