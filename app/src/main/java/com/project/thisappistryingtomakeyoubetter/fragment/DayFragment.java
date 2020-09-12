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
import com.project.thisappistryingtomakeyoubetter.databinding.DialogTaskBinding;
import com.project.thisappistryingtomakeyoubetter.model.Task;
import com.project.thisappistryingtomakeyoubetter.util.AppDatabase;
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper;
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity;
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentDayBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class DayFragment extends Fragment implements
        View.OnClickListener,
        TaskAdapter.TaskCallback{

    // Static Variables
    public final static String DATE = "date";
    public final static String POSITION = "position";

    private Calendar calendar;
    private FragmentDayBinding binding;
    private List<Task> tasks = new ArrayList<>();
    private TaskAdapter taskAdapter;
    private AppDatabase db;
    private Date from, to;
    private int position;

    public DayFragment() {
        // Required empty public constructor
    }

    public static DayFragment newInstance(Calendar calendar, int position) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putLong(DATE, calendar.getTimeInMillis());
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH);
        calendar.setTimeInMillis(getArguments().getLong(DATE));
        position = getArguments().getInt(POSITION);
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

        db = Room.databaseBuilder(requireContext(), AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();

        from = GeneralHelper.fromDate(calendar);
        to = GeneralHelper.toDate(calendar);

        taskAdapter = new TaskAdapter(getActivity(), tasks, this);
        binding.task.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.task.setAdapter(taskAdapter);

        // Floating Action Button Add
        binding.addTask.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        String title;
        switch (position){
            case 0:
                title = getString(R.string.title_today);
                break;
            case 1:
                title = getString(R.string.title_tomorrow);
                break;
            default:
                title = GeneralHelper.dateFormatter().format(calendar.getTime());
                break;
        }
        ((MainActivity)requireActivity()).toolbar.setTitle(title);

        getAll(from, to);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_task) {
            taskDialog(null);
        }
    }

    @Override
    public void onLongClick(Task task) {
        taskDialog(task);
    }

    @Override
    public void onBoxChecked(Task task) {
        updateTask(task);
    }

    private void taskDialog(final Task task){
        final Dialog dialog = new Dialog(requireContext());
        final DialogTaskBinding binding = DialogTaskBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Hide Keyboard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        // Match dialog window to screen width
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // Views Setup
        if(task != null){
            binding.title.setText(task.getTitle());
            binding.description.setText(task.getDescription());
            binding.delete.setVisibility(View.VISIBLE);
        }

        // Listeners
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task == null) {
                    Task task = new Task(binding.title.getText().toString(),
                            binding.description.getText().toString(),
                            calendar.getTime());
                    addTask(task);
                } else {
                    task.setTitle(binding.title.getText().toString());
                    task.setDescription(binding.description.getText().toString());
                    updateTask(task);
                }
                dialog.dismiss();
            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task != null) {
                    deleteTask(task);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getAll(Date from, Date to){
        tasks.clear();
        tasks.addAll(db.taskDao().getAll(from, to));

        if(taskAdapter != null) {
            taskAdapter.notifyDataSetChanged();
        }
        placeHolder();
    }

    private void addTask(Task task) {
        db.taskDao().insertAll(task);
        getAll(from, to);
    }

    private void deleteTask(Task task) {
        db.taskDao().delete(task);
        getAll(from, to);
    }

    private void updateTask(Task task){
        db.taskDao().update(task);
        getAll(from, to);
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