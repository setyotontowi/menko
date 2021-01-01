package com.project.thisappistryingtomakeyoubetter.di

import com.project.thisappistryingtomakeyoubetter.fragment.HistoryFragment
import com.project.thisappistryingtomakeyoubetter.fragment.LabelFragment
import com.project.thisappistryingtomakeyoubetter.fragment.MainFragment
import dagger.Module
import dagger.Provides

@Module
object FragmentModule {
    @Provides
    fun providesHistoryFragment() = HistoryFragment.newInstance()

    @Provides
    fun providesMainFragment() = MainFragment.newInstance()

    @Provides
    fun providesLabelFragment() = LabelFragment.newInstance()
}