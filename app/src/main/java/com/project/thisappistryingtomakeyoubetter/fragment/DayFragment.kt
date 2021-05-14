package com.project.thisappistryingtomakeyoubetter.fragment;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.project.thisappistryingtomakeyoubetter.App;
import com.project.thisappistryingtomakeyoubetter.R;
import com.project.thisappistryingtomakeyoubetter.adapter.ChipAdapter;
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter;
import com.project.thisappistryingtomakeyoubetter.databinding.DialogTaskBinding;
import com.project.thisappistryingtomakeyoubetter.model.Label;
import com.project.thisappistryingtomakeyoubetter.model.Task;
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel;
import com.project.thisappistryingtomakeyoubetter.util.AppDatabase;
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper;
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity;
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentDayBinding;
import com.project.thisappistryingtomakeyoubetter.util.LabelViewModel;
import com.project.thisappistryingtomakeyoubetter.util.TaskViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Named;

public class DayFragment extends Fragment implements
        View.OnClickListener,
        TaskAdapter.TaskCallback{

    // Static Variables
    private static final String TAG = "DayFragment";
    public final static String DATE = "date";
    public final static String POSITION = "position";

    private Calendar calendar;
    private FragmentDayBinding binding;
    private final List<TaskWithLabel> tasks = new ArrayList<>();
    private final List<Label> labels = new ArrayList<>();
    private TaskAdapter taskAdapter;
    private int position;
    private TaskViewModel taskViewModel;
    @Inject
    ViewModelProvider.Factory vmFactory;
    Date from;
    Date to;

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
        ((App) requireActivity().getApplication()).getAppComponent().inject(this);
        setHasOptionsMenu(true);
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

        from = GeneralHelper.fromDate(calendar);
        to = GeneralHelper.toDate(calendar);

        taskAdapter = new TaskAdapter(requireActivity(), tasks, this, GeneralHelper.MODE_DAY);
        binding.task.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.task.setAdapter(taskAdapter);

        taskViewModel = new ViewModelProvider(this, vmFactory)
                .get(TaskViewModel.class);

        getLabel();

        // Floating Action Button Add
        binding.addTask.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getTaskWithLabel();
        taskAdapter.notifyDataSetChanged();
        String title;
        switch (position){
            case -1:
                title = getString(R.string.title_yesterday);
                break;
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
        ((MainActivity)requireParentFragment().requireActivity()).toolbar.setTitle(title);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_task) {
            taskDialog(null);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onLongClick(TaskWithLabel task) {
        taskDialog(task);
    }

    @Override
    public void onBoxChecked(TaskWithLabel task) { updateTask(task.getTask()); }

    private void taskDialog(final TaskWithLabel task){
        final Dialog dialog = new Dialog(requireContext());
        final DialogTaskBinding binding = DialogTaskBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Hide Keyboard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        // Chip Adapter
        ChipAdapter chipAdapter = new ChipAdapter(requireContext(), labels);

        // Match dialog window to screen width
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        // Views Setup
        if(task != null){
            chipAdapter.setSelectedLabels(task.getLabels());
            binding.title.setText(task.getTask().getTitle());
            binding.description.setText(task.getTask().getDescription());
            binding.delete.setVisibility(View.VISIBLE);
        }
        binding.labels.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.labels.setAdapter(chipAdapter);


        // Listeners
        binding.save.setOnClickListener(v -> {
            if(task == null) {
                Task task1 = new Task(Objects.requireNonNull(binding.title.getText()).toString(),
                        Objects.requireNonNull(binding.description.getText()).toString(),
                        calendar.getTime());
                task1.setLabels(chipAdapter.getSelectedLabels());
                addTask(task1);
            } else {
                task.getTask().setTitle(Objects.requireNonNull(binding.title.getText()).toString());
                task.getTask().setDescription(Objects.requireNonNull(binding.description.getText()).toString());
                task.getTask().setLabels(chipAdapter.getSelectedLabels());
                updateTask(task.getTask());
            }
            dialog.dismiss();
        });

        binding.delete.setOnClickListener(v -> {
            if(task != null) {
                deleteTask(task.getTask());
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void getLabel(){
        taskViewModel.getLabel().observe(getViewLifecycleOwner(), labels -> {
            Log.d(TAG, "getLabel: Observer Label");
            this.labels.clear();
            this.labels.addAll(labels);
        });
    }

    private void getTaskWithLabel(){
        taskViewModel.getTaskWithLabel(from, to).observe(getViewLifecycleOwner(), tasksWithLabels -> {
            Log.d(TAG, "getTaskWithLabel: Observer TaskWithLabel");
            DayFragment.this.tasks.clear();
            DayFragment.this.tasks.addAll(tasksWithLabels);
            taskAdapter.notifyDataSetChanged();
            placeHolder();
        });
    }

    private void addTask(Task task) {
        taskViewModel.insert(task);
    }
    
    private void deleteTask(Task task) {
        taskViewModel.delete(task);
    }

    private void updateTask(Task task){
        taskViewModel.update(task);
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