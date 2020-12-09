package com.project.thisappistryingtomakeyoubetter.util

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.getOrAwaitValue
import com.project.thisappistryingtomakeyoubetter.model.Task
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

@ExperimentalMultiplatform
@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTask {
    private lateinit var taskDao: TaskDao
    private lateinit var appDatabase: AppDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java
        ).allowMainThreadQueries().build()
        taskDao = appDatabase.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertTask() {
        runBlocking {
            val task = Task("Todo", "Description")
            task.id = 1
            task.date = Date()
            appDatabase.taskDao().insertAll(task)

            val allTask = taskDao.getAll.getOrAwaitValue()
            assert(check(allTask, task))
        }
    }

    fun check(tasks: List<Task>?, task: Task): Boolean{
        if (tasks != null) {
            for(t in tasks){
                if(t.id == task.id) return true
            }
        }
        return false
    }
}