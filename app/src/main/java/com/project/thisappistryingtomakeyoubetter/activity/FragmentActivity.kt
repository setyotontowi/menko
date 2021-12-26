package com.project.thisappistryingtomakeyoubetter.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.databinding.ActivityFragmentBinding
import com.project.thisappistryingtomakeyoubetter.fragment.HistoryFragment
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.viewmodel.MainViewModel
import com.project.thisappistryingtomakeyoubetter.viewmodel.TaskViewModel
import javax.inject.Inject

class FragmentActivity: AppCompatActivity() {

    val historyFragment: HistoryFragment by lazy {
        HistoryFragment.newInstance()
    }

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    lateinit private var taskViewModel: TaskViewModel
    lateinit private var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as App).appComponent.inject(this)

        taskViewModel = ViewModelProvider(this, vmFactory).get(TaskViewModel::class.java)
        mainViewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)

        binding.apply {
            val label = intent.getSerializableExtra(HistoryFragment.EXTRA_FILTER) as Label
            taskViewModel.filter(listOf(label))
            mainViewModel.standAlone.postValue(true)
            mainViewModel.stateFromOutsideMainFragment.postValue(false)

            openFragment(historyFragment)
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}