package com.project.thisappistryingtomakeyoubetter.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.project.thisappistryingtomakeyoubetter.activity.MainActivity;
import com.project.thisappistryingtomakeyoubetter.fragment.DayFragment;

import java.util.Calendar;
import java.util.List;

public class DayAdapter extends FragmentStateAdapter {
    List<Calendar> calendars;

    public DayAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle, List<Calendar> calendars) {
        super(fragmentManager, lifecycle);
        this.calendars = calendars;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Calendar calendar = calendars.get(position);
        if(MainActivity.INCLUDE_YESTERDAY) position--;
        return DayFragment.newInstance(calendar, position);
    }

    @Override
    public int getItemCount() {
        return calendars.size();
    }
}
