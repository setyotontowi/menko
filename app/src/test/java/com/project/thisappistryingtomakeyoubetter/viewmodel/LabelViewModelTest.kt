package com.project.thisappistryingtomakeyoubetter.viewmodel

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.util.LabelRepository
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.atLeast
import org.junit.Rule


@RunWith(JUnit4::class)
class LabelViewModelTest : TestCase() {

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: LabelViewModel

    @Mock
    lateinit var labelRepository: LabelRepository
    @Mock
    lateinit var observer: Observer<List<Label>?>

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        viewModel = LabelViewModel(labelRepository)
        viewModel.labels.observeForever(observer)
    }

    @Test
    fun testGetLabels() {
        viewModel.insert(Label(1, "Hello", 1))
        viewModel.get()
        assertEquals(viewModel.labels.value, listOf(Label(1, "Hello", 1)))
    }

    fun testInsert() {}

    fun testUpdate() {}

    fun testDelete() {}

    fun testDeleteAll() {}

    fun testGet() {}

    fun testGetLabelWithTask() {}
}