package com.project.thisappistryingtomakeyoubetter.fragment;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.thisappistryingtomakeyoubetter.R;

public class IntroFirstPage extends Fragment {

    public IntroFirstPage() {
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
        return inflater.inflate(R.layout.fragment_intro_first_page, container, false);
    }
}