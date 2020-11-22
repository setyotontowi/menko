package com.project.thisappistryingtomakeyoubetter.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.project.thisappistryingtomakeyoubetter.util.AppDatabase
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors

@Module
object DatabaseModule {

    private const val DATABASE_NAME = "database"

    @Provides
    fun providesDatabase(appContext: Context): AppDatabase{
        return Room.databaseBuilder(appContext.applicationContext,
                AppDatabase::class.java, DATABASE_NAME)
                .build()
    }

    @Provides
    fun providesTaskDao(db: AppDatabase) = db.taskDao()

}