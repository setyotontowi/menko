package com.project.thisappistryingtomakeyoubetter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.adapter.FragmentViewHolder;


import android.view.Menu;
import android.view.MenuItem;

import com.project.thisappistryingtomakeyoubetter.App;
import com.project.thisappistryingtomakeyoubetter.adapter.DayAdapter;
import com.project.thisappistryingtomakeyoubetter.util.DepthPageTransformer;
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper;
import com.project.thisappistryingtomakeyoubetter.R;
import com.project.thisappistryingtomakeyoubetter.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(GeneralHelper.dateFormatter().format(getDate()));
        setSupportActionBar(toolbar);

        ((App) getApplication()).getAppComponent().inject(this);

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

        // Generate Calendar List
        calendar = generateCalendar(DAY_LIMIT, INCLUDE_YESTERDAY);

        // Create Fragment View Pager
        DayAdapter dayAdapter = new DayAdapter(getSupportFragmentManager(),
                getLifecycle(),
                calendar);
        binding.frame.setAdapter(dayAdapter);
        binding.frame.setPageTransformer(new DepthPageTransformer());
        binding.frame.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Date getDate(){
        return new Date();
    }

    /**
     * Generating Calendar, a temporary method.
     * @param limit: choose until what day
     * @return List of Calendar (Today, Tomorrow, until limit)
     */
    private List<Calendar> generateCalendar(int limit, boolean includeYesterday){
        List<Calendar> calendars = new ArrayList<>();

        for (int i=includeYesterday?-1:0; i<limit; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, i);
            calendars.add(calendar);
        }
        return calendars;
    }

}