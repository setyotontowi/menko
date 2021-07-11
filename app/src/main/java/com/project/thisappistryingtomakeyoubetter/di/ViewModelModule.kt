package com.project.thisappistryingtomakeyoubetter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.thisappistryingtomakeyoubetter.viewmodel.LabelViewModel
import com.project.thisappistryingtomakeyoubetter.viewmodel.MainViewModel
import com.project.thisappistryingtomakeyoubetter.viewmodel.TaskViewModel
import com.project.thisappistryingtomakeyoubetter.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel::class)
    internal abstract fun taskViewModel(viewModel: TaskViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LabelViewModel::class)
    internal abstract fun labelViewModel(viewModel: LabelViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel
}