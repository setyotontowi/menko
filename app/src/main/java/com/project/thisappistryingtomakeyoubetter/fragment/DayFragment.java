package com.project.thisappistryingtomakeyoubetter.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.project.thisappistryingtomakeyoubetter.R;
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter;
import com.project.thisappistryingtomakeyoubetter.databinding.DialogAddTaskBinding;
import com.project.thisappistryingtomakeyoubetter.model.Task;
import com.project.thisappistryingtomakeyoubetter.util.AppDatabase;
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper;
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity;
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentDayBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DayFragment extends Fragment implements
        View.OnClickListener,
        TaskAdapter.LongClick{

    private Calendar calendar;
    private FragmentDayBinding binding;
    private List<Task> tasks;
    private TaskAdapter taskAdapter;
    private AppDatabase db;
    private Date from, to;

    public DayFragment() {
        // Required empty public constructor
    }

    public static DayFragment newInstance(Calendar calendar) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putLong("date", calendar.getTimeInMillis());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getArguments().getLong("date"));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tasks = new ArrayList<>();

        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();

        from = GeneralHelper.fromDate(calendar);
        to = GeneralHelper.toDate(calendar);

        getAll(from, to);

        placeHolder();

        taskAdapter = new TaskAdapter(getActivity(), tasks, this);
        binding.task.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.task.setAdapter(taskAdapter);

        // Floating Action Button Add
        binding.addTask.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)requireActivity()).toolbar.setTitle(
                GeneralHelper.dateFormatter().format(calendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_task) {
            addTaskDialog();
        }
    }

    @Override
    public void onLongClick(Task task) {
        deleteTask(task);
    }

    private void addTaskDialog(){
        final Dialog dialog = new Dialog(requireContext());
        final DialogAddTaskBinding binding = DialogAddTaskBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask(binding.title.getText().toString(),
                        binding.description.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getAll(Date from, Date to){
        tasks = db.taskDao().getAll(from, to);
    }

    private void addTask(String title, String description) {
        Task task = new Task(title, description, GeneralHelper.toDate(calendar));
        tasks.add(task);
        taskAdapter.notifyDataSetChanged();
        placeHolder();

        db.taskDao().insertAll(task);
    }

    private void deleteTask(Task task) {
        db.taskDao().delete(task);
        getAll(from, to);
        taskAdapter.notifyDataSetChanged();
        placeHolder();
    }

    private void placeHolder(){
        if(tasks.isEmpty()){
            binding.task.setVisibility(View.GONE);
            binding.nodata.setVisibility(View.VISIBLE);
        } else {
            binding.task.setVisibility(View.VISIBLE);
            binding.nodata.setVisibility(View.GONE);
        }
    }
}