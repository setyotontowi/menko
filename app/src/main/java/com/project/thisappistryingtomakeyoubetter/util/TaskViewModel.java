package com.project.thisappistryingtomakeyoubetter.util;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.Date;
import java.util.List;

public class TaskViewModel extends AndroidViewModel implements ViewModelProvider.Factory {

    private final Application application;
    private final Date from, to;
    private final TaskRepository taskRepository;
    private final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application, Date from, Date to) {
        super(application);
        this.application = application;
        this.from = from;
        this.to = to;
        taskRepository = new TaskRepository(application, from, to);
        allTasks = taskRepository.getTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        taskRepository.insert(task);
    }

    public void update(Task task) {
        taskRepository.update(task);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TaskViewModel(application, from, to);
    }
}
