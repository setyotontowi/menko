package com.project.thisappistryingtomakeyoubetter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.thisappistryingtomakeyoubetter.databinding.FragmentDayBinding;

import java.util.Calendar;
import java.util.Date;

public class DayFragment extends Fragment {

    private Date date;
    private FragmentDayBinding binding;

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
        date = new Date(getArguments().getLong("date"));
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
        binding.date.setText(GeneralHelper.dateFormatter().format(date));

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)requireActivity()).toolbar.setTitle(
                GeneralHelper.dateFormatter().format(date));
    }
}