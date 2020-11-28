package com.project.thisappistryingtomakeyoubetter.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.project.thisappistryingtomakeyoubetter.util.AppDatabase
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import dagger.Module
import dagger.Provides
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Named

@Module
object DatabaseModule {

    private const val DATABASE_NAME = "database"

    @Provides
    fun providesDatabase(appContext: Context): AppDatabase{
        return Room.databaseBuilder(appContext.applicationContext,
                AppDatabase::class.java, DATABASE_NAME)
                .build()
    }

    /*@Provides
    @Named("From")
    fun providesFrom(calendar: Calendar): Date{
        return GeneralHelper.fromDate(calendar)
    }

    @Provides
    @Named("To")
    fun providesTo(calendar: Calendar): Date{
        return GeneralHelper.toDate(calendar)
    }*/

    @Provides
    fun providesTaskDao(db: AppDatabase) = db.taskDao()

}