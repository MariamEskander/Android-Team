package com.ibtikar.mada.di

import com.ibtikar.mada.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class Modules {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

}
