package com.project.thisappistryingtomakeyoubetter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity
import com.project.thisappistryingtomakeyoubetter.adapter.DayAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentMainBinding
import com.project.thisappistryingtomakeyoubetter.util.DepthPageTransformer
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import com.project.thisappistryingtomakeyoubetter.viewmodel.MainViewModel
import java.util.*
import javax.inject.Inject


class MainFragment : Fragment() {

    private var calendar: List<Calendar>? = null
    private var currentItem = if (MainActivity.INCLUDE_YESTERDAY) 1 else 0
    private lateinit var binding: FragmentMainBinding
    lateinit var dayAdapter: DayAdapter
    lateinit var viewPager: ViewPager2
    val viewModel: MainViewModel by activityViewModels()

    @JvmField
    @Inject
    var vmFactory: ViewModelProvider.Factory? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.inject(this)

        createDay()
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.stateFromOutsideMainFragment.value == false){
            createDay()
        } else {
            currentItem = viewModel.currentPosition.value?:(if (MainActivity.INCLUDE_YESTERDAY) 1 else 0)
            viewPager.doOnPreDraw {
                viewPager.currentItem = currentItem
                viewPager.invalidate()
            }
            (requireActivity() as MainActivity).toolbar.title = viewModel.dayTitle.value.toString()
        }
    }

    override fun onPause() {
        super.onPause()
        currentItem = viewPager.currentItem
        viewModel.currentPosition.value = viewPager.currentItem
        viewModel.dayTitle.value = (requireActivity() as MainActivity).toolbar.title.toString()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if(!hidden){
            (requireActivity() as MainActivity).toolbar.title = viewModel.dayTitle.value.toString()
        } else {
            viewModel.dayTitle.value = (requireActivity() as MainActivity).toolbar.title.toString()
        }
        super.onHiddenChanged(hidden)
    }

    private fun createDay() {
        // Generate Calendar List
        calendar = generateCalendar(MainActivity.DAY_LIMIT, MainActivity.INCLUDE_YESTERDAY)

        // Create Fragment View Pager
        viewPager = binding.frame
        viewPager.offscreenPageLimit = 5
        dayAdapter = DayAdapter(
                childFragmentManager,
                lifecycle,
                calendar
        )
        viewPager.adapter = dayAdapter
        //viewPager.setPageTransformer(DepthPageTransformer())
        TabLayoutMediator(binding.tabLayout, viewPager){ tab, position ->
            val title: String = when (position) {
                0 -> if(MainActivity.INCLUDE_YESTERDAY) getString(R.string.title_yesterday) else getString(R.string.title_today)
                1 -> if(MainActivity.INCLUDE_YESTERDAY) getString(R.string.title_today) else getString(R.string.title_tomorrow)
                2 -> if(MainActivity.INCLUDE_YESTERDAY) getString(R.string.title_tomorrow) else GeneralHelper.dayFormatter().format(calendar!![position].time)
                else -> GeneralHelper.dayFormatter().format(calendar!![position].time)
            }
            tab.text = title
        }.attach()
    }

    /**
     * Generating Calendar, a temporary method.
     * @param limit: choose until what day
     * @param includeYesterday : if user also want include yesterday fragment in viewpager
     * @return List of Calendar (Today, Tomorrow, until limit)
     */
    private fun generateCalendar(limit: Int, includeYesterday: Boolean): List<Calendar> {
        val calendars: MutableList<Calendar> = ArrayList()
        val s = if (includeYesterday) -1 else 0
        for (i in s until limit) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, i)
            calendars.add(calendar)
        }
        return calendars
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}