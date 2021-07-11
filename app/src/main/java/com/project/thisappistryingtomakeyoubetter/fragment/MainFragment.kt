package com.project.thisappistryingtomakeyoubetter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity
import com.project.thisappistryingtomakeyoubetter.adapter.DayAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentMainBinding
import com.project.thisappistryingtomakeyoubetter.util.DepthPageTransformer
import com.project.thisappistryingtomakeyoubetter.viewmodel.MainViewModel
import java.util.*
import javax.inject.Inject


class MainFragment : Fragment() {

    private var calendar: List<Calendar>? = null
    private var currentItem = if (MainActivity.INCLUDE_YESTERDAY) 1 else 0
    private lateinit var binding: FragmentMainBinding
    lateinit var dayAdapter: DayAdapter
    lateinit var viewPager: ViewPager2
    lateinit var viewModel: MainViewModel

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

        // Set ViewModel
        viewModel = ViewModelProvider(requireActivity(), vmFactory!!).get(MainViewModel::class.java)

        // Generate Calendar List
        calendar = generateCalendar(MainActivity.DAY_LIMIT, MainActivity.INCLUDE_YESTERDAY)

        // Create Fragment View Pager
        viewPager = binding.frame
        dayAdapter = DayAdapter(
            childFragmentManager,
            lifecycle,
            calendar
        )
        viewPager.adapter = dayAdapter
        viewPager.setPageTransformer(DepthPageTransformer())
    }

    override fun onResume() {
        super.onResume()
        viewPager.currentItem = viewModel.currentPosition.value?:(if (MainActivity.INCLUDE_YESTERDAY) 1 else 0)
    }

    override fun onPause() {
        super.onPause()
        currentItem = viewPager.currentItem
        viewModel.currentPosition.value = viewPager.currentItem
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