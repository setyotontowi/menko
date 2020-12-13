package com.project.thisappistryingtomakeyoubetter.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.project.thisappistryingtomakeyoubetter.util.AppDatabase
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import com.project.thisappistryingtomakeyoubetter.util.LabelDao
import com.project.thisappistryingtomakeyoubetter.util.TaskDao
import dagger.Module
import dagger.Provides
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Singleton

@Module
object DatabaseModule {

    private const val DATABASE_NAME = "database"

    @Provides
    fun providesDatabase(appContext: Context): AppDatabase{
        return Room
                .databaseBuilder(
                    appContext.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME)
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
    }

    // TODO: 13/12/2020 (2) Provides LabelDao (complete)
    // TODO: 13/12/2020 (4) Migration (complete)
    
    @Provides
    fun providesTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    fun providesLabelDao(db: AppDatabase): LabelDao = db.labelDao();

}