package com.project.thisappistryingtomakeyoubetter.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.thisappistryingtomakeyoubetter.R;

public class IntroSecondPage extends Fragment {

    public IntroSecondPage() {
        // Required empty public constructor
    }

    public static IntroSecondPage newInstance() {
        return new IntroSecondPage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_second_page, container, false);
    }
}