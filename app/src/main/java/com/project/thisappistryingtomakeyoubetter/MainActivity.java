package com.project.thisappistryingtomakeyoubetter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(GeneralHelper.dateFormatter().format(getDate()));
        setSupportActionBar(toolbar);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Shared Preferences init
                SharedPreferences getPrefs = getSharedPreferences(getString(R.string.prefSetting),
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = getPrefs.edit();

                // Check if apps run on first start
                boolean firstStart = getPrefs.getBoolean("firstStart", true);
                if(firstStart){
                    final Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }
                    });

                    // Edit firstStart to false
                    editor.putBoolean("firstStart", false);
                    editor.apply();
                }
            }
        });

        t.start();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Date getDate(){
        return new Date();
    }
}