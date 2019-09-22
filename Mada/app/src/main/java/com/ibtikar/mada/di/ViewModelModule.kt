package com.ibtikar.mada.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ibtikar.mada.ui.MainViewModel
import com.ibtikar.mada.base.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

}
