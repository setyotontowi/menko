package com.project.thisappistryingtomakeyoubetter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.thisappistryingtomakeyoubetter.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}