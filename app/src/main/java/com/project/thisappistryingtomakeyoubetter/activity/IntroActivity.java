package com.project.thisappistryingtomakeyoubetter.activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroPageTransformerType;
import com.project.thisappistryingtomakeyoubetter.fragment.IntroFirstPage;
import com.project.thisappistryingtomakeyoubetter.fragment.IntroSecondPage;
import com.project.thisappistryingtomakeyoubetter.fragment.IntroThirdPage;
import com.project.thisappistryingtomakeyoubetter.R;


public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new IntroFirstPage());
        addSlide(new IntroSecondPage());
        addSlide(new IntroThirdPage());

        showStatusBar(false);
        setTransformer(AppIntroPageTransformerType.Zoom.INSTANCE);
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }
}