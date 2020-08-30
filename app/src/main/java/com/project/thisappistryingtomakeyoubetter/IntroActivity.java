package com.project.thisappistryingtomakeyoubetter;

import androidx.annotation.Nullable;

import android.os.Bundle;

import com.github.appintro.AppIntro;


public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new IntroFirstPage());
        addSlide(new IntroSecondPage());
        addSlide(new IntroThirdPage());
    }
}