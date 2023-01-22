package com.project.thisappistryingtomakeyoubetter.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.databinding.ActivityFragmentBinding
import com.project.thisappistryingtomakeyoubetter.fragment.HistoryFragment
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.viewmodel.MainViewModel
import com.project.thisappistryingtomakeyoubetter.viewmodel.DayViewModel
import com.project.thisappistryingtomakeyoubetter.viewmodel.HistoryViewModel
import javax.inject.Inject

class FragmentActivity: AppCompatActivity() {

    val historyFragment: HistoryFragment by lazy {
        val fragment = HistoryFragment.newInstance()
        fragment
    }

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    lateinit private var taskViewModel: HistoryViewModel
    lateinit private var mainViewModel: MainViewModel

    private lateinit var label: Label

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as App).appComponent.inject(this)

        taskViewModel = ViewModelProvider(this, vmFactory).get(HistoryViewModel::class.java)
        mainViewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)

        binding.apply {
            label = intent.getSerializableExtra(HistoryFragment.EXTRA_FILTER) as Label

            toolbar.title = label.name

            taskViewModel.filterLabel = listOf(label)
            taskViewModel.filter()

            openFragment(historyFragment)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun openFragment(fragment: Fragment) {
        fragment.arguments = bundleOf().apply {
            putInt(HistoryFragment.EXTRA_PEEK_HEIGHT, 1850)
            putSerializable(HistoryFragment.EXTRA_FILTER, label)
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}