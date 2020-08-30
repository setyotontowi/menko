package com.project.thisappistryingtomakeyoubetter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class IntroThirdPage extends Fragment {

    public IntroThirdPage() {
        // Required empty public constructor
    }

    public static IntroThirdPage newInstance() {
        IntroThirdPage fragment = new IntroThirdPage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_third_page, container, false);
    }
}