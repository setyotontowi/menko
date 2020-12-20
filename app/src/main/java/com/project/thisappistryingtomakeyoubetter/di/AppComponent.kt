package com.project.thisappistryingtomakeyoubetter.di

import android.content.Context
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity
import com.project.thisappistryingtomakeyoubetter.fragment.DayFragment
import com.project.thisappistryingtomakeyoubetter.fragment.HistoryFragment
import com.project.thisappistryingtomakeyoubetter.fragment.LabelFragment
import com.project.thisappistryingtomakeyoubetter.fragment.MainFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(fragment: DayFragment)
    fun inject(historyFragment: HistoryFragment)
    fun inject(labelFragment: LabelFragment)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance appContext: Context): AppComponent
    }
}