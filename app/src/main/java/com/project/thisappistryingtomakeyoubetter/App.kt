package com.project.thisappistryingtomakeyoubetter

import android.app.Application
import com.project.thisappistryingtomakeyoubetter.di.AppComponent
import com.project.thisappistryingtomakeyoubetter.di.DaggerAppComponent

class App: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}