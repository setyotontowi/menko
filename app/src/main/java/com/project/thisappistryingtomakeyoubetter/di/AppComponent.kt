package com.project.thisappistryingtomakeyoubetter.di

import android.content.Context
import com.project.thisappistryingtomakeyoubetter.activity.HistoryActivity
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity
import com.project.thisappistryingtomakeyoubetter.fragment.DayFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(fragment: DayFragment)
    fun inject(historyActivity: HistoryActivity)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance appContext: Context): AppComponent
    }
}