package com.project.thisappistryingtomakeyoubetter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity
import com.project.thisappistryingtomakeyoubetter.adapter.DayAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentMainBinding
import com.project.thisappistryingtomakeyoubetter.util.DepthPageTransformer
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import java.util.*


class MainFragment : Fragment() {

    private var calendar: List<Calendar>? = null
    private var currentItem = if (MainActivity.INCLUDE_YESTERDAY) 1 else 0
    private lateinit var binding: FragmentMainBinding
    private lateinit var dayAdapter: DayAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.inject(this)

        // Generate Calendar List
        calendar = generateCalendar(MainActivity.DAY_LIMIT, MainActivity.INCLUDE_YESTERDAY)

        // Create Fragment View Pager
        dayAdapter = DayAdapter(childFragmentManager,
                lifecycle,
                calendar)
        binding.frame.adapter = dayAdapter
        binding.frame.setPageTransformer(DepthPageTransformer())
    }

    override fun onResume() {
        super.onResume()
        binding.frame.currentItem = currentItem
    }

    override fun onPause() {
        super.onPause()
        currentItem = binding.frame.currentItem
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