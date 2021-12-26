package com.project.thisappistryingtomakeyoubetter.di

import android.content.Context
import com.project.thisappistryingtomakeyoubetter.activity.FragmentActivity
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity
import com.project.thisappistryingtomakeyoubetter.fragment.DayFragment
import com.project.thisappistryingtomakeyoubetter.fragment.HistoryFragment
import com.project.thisappistryingtomakeyoubetter.fragment.LabelFragment
import com.project.thisappistryingtomakeyoubetter.fragment.MainFragment
import com.project.thisappistryingtomakeyoubetter.model.Label
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, ViewModelModule::class, FragmentModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(dayFragment: DayFragment)
    fun inject(historyFragment: HistoryFragment)
    fun inject(labelFragment: LabelFragment)
    fun inject(fragmentActivity: FragmentActivity)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance appContext: Context): AppComponent
    }
}