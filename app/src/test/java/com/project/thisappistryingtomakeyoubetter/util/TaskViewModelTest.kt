package com.project.thisappistryingtomakeyoubetter.util

import android.util.Log
import com.project.thisappistryingtomakeyoubetter.model.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TaskViewModelTest {

    private lateinit var taskViewModel: TaskViewModel

    @Mock
    private lateinit var taskRepository: TaskRepository

    @Mock
    private lateinit var labelRepository: LabelRepository

    @Before
    fun setup() {
        taskViewModel = TaskViewModel(taskRepository, labelRepository)
    }

    @Test
    fun insert() {
        taskViewModel.insert(Task("Title", "Desc"))
        println("Haaha")
    }
}