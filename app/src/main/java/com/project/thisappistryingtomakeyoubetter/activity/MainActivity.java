package com.project.thisappistryingtomakeyoubetter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.adapter.FragmentViewHolder;


import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.thisappistryingtomakeyoubetter.App;
import com.project.thisappistryingtomakeyoubetter.adapter.DayAdapter;
import com.project.thisappistryingtomakeyoubetter.fragment.HistoryFragment;
import com.project.thisappistryingtomakeyoubetter.fragment.LabelFragment;
import com.project.thisappistryingtomakeyoubetter.fragment.MainFragment;
import com.project.thisappistryingtomakeyoubetter.util.DepthPageTransformer;
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper;
import com.project.thisappistryingtomakeyoubetter.R;
import com.project.thisappistryingtomakeyoubetter.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private List<Calendar> calendar;
    public static final int DAY_LIMIT = 5;
    public static final boolean INCLUDE_YESTERDAY = true;
    private ActivityMainBinding binding;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ((App) getApplication()).getAppComponent().inject(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // App Intro Initiation
        Thread t = new Thread(() -> {
            // Shared Preferences
            SharedPreferences getPrefs = getSharedPreferences(getString(R.string.prefSetting),
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = getPrefs.edit();

            // Check if apps run on first start
            boolean firstStart = getPrefs.getBoolean("firstStart", true);
            if(firstStart){
                final Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                runOnUiThread(() -> startActivity(intent));

                // Edit firstStart to false
                editor.putBoolean("firstStart", false);
                editor.apply();
            }
        });

        t.start();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_view);
        NavController navController = navHostFragment != null ? navHostFragment.getNavController() : null;

        Navigation.setViewNavController(binding.navView, navController);

        Fragment mainFragment = MainFragment.newInstance();
        Fragment historyFragment = HistoryFragment.newInstance();
        Fragment labelFragment = LabelFragment.newInstance();
        // First Fragment Shown
        openFragment(mainFragment);


        binding.navView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.action_main){
                openFragment(mainFragment);
                return true;
            } else if (item.getItemId() == R.id.action_history){
                toolbar.setTitle("History");
                openFragment(historyFragment);
                return true;
            } else if (item.getItemId() == R.id.action_label){
                toolbar.setTitle("Label");
                openFragment(labelFragment);
                return true;
            }
            return false;
        });

    }

    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



}