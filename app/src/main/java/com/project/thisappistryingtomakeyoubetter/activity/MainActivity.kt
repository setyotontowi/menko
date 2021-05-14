package com.project.thisappistryingtomakeyoubetter.activity

import com.project.thisappistryingtomakeyoubetter.fragment.MainFragment.Companion.newInstance
import com.project.thisappistryingtomakeyoubetter.fragment.HistoryFragment.Companion.newInstance
import com.project.thisappistryingtomakeyoubetter.fragment.LabelFragment.Companion.newInstance
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.project.thisappistryingtomakeyoubetter.activity.IntroActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.project.thisappistryingtomakeyoubetter.fragment.MainFragment
import com.project.thisappistryingtomakeyoubetter.fragment.HistoryFragment
import com.project.thisappistryingtomakeyoubetter.fragment.LabelFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.thisappistryingtomakeyoubetter.databinding.ActivityMainBinding
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val calendar: List<Calendar>? = null
    private var binding: ActivityMainBinding? = null
    lateinit var toolbar: Toolbar

    @Inject
    lateinit var mainFragment: MainFragment
    @Inject
    lateinit var historyFragment: HistoryFragment
    @Inject
    lateinit var labelFragment: LabelFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        (application as App).appComponent.inject(this)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // App Intro Initiation
        val t = Thread {

            // Shared Preferences
            val getPrefs = getSharedPreferences(getString(R.string.prefSetting),
                    MODE_PRIVATE)
            val editor = getPrefs.edit()

            // Check if apps run on first start
            val firstStart = getPrefs.getBoolean("firstStart", true)
            if (firstStart) {
                val intent = Intent(this@MainActivity, IntroActivity::class.java)
                runOnUiThread { startActivity(intent) }

                // Edit firstStart to false
                editor.putBoolean("firstStart", false)
                editor.apply()
            }
        }
        t.start()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_view) as NavHostFragment?
        val navController = navHostFragment?.navController
        Navigation.setViewNavController(binding!!.navView, navController)

        openFragment(mainFragment)

        binding!!.navView.setOnNavigationItemSelectedListener { item: MenuItem ->
            if (item.itemId == R.id.action_main) {
                openFragment(mainFragment)
                return@setOnNavigationItemSelectedListener true
            } else if (item.itemId == R.id.action_history) {
                toolbar.title = "History"
                openFragment(historyFragment)
                return@setOnNavigationItemSelectedListener true
            } else if (item.itemId == R.id.action_label) {
                toolbar.title = "Label"
                openFragment(labelFragment)
                return@setOnNavigationItemSelectedListener true
            }
            false
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        Navigation.findNavController(this, R.id.nav_host_fragment).popBackStack()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    companion object {
        const val DAY_LIMIT = 5
        const val INCLUDE_YESTERDAY = true
    }
}