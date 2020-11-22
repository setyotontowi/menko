package com.project.thisappistryingtomakeyoubetter.di

import android.content.Context
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance appContext: Context): AppComponent
    }
}