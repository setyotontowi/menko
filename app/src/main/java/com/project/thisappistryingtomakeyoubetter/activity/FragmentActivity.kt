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
import com.project.thisappistryingtomakeyoubetter.viewmodel.DayViewModel
import javax.inject.Inject

class FragmentActivity: AppCompatActivity() {

    val historyFragment: HistoryFragment by lazy {
        val fragment = HistoryFragment.newInstance()
        val bundle = Bundle().apply{
            putInt(HistoryFragment.EXTRA_PEEK_HEIGHT, 1850)
        }
        fragment.arguments = bundle
        fragment
    }

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    lateinit private var taskViewModel: DayViewModel
    lateinit private var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as App).appComponent.inject(this)

        taskViewModel = ViewModelProvider(this, vmFactory).get(DayViewModel::class.java)
        mainViewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)

        binding.apply {
            val label = intent.getSerializableExtra(HistoryFragment.EXTRA_FILTER) as Label

            toolbar.title = label.name

            taskViewModel.filter(listOf(label))
            mainViewModel.standAlone.value = true
            mainViewModel.stateFromOutsideMainFragment.postValue(false)

            openFragment(historyFragment)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}