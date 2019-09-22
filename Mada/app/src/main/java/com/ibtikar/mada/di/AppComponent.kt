package com.ibtikar.mada.di

import android.app.Application
import android.content.Context
import com.ibtikar.mada.app.MadaApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Mariam Eskander on 2019-06-05.
 */

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, Modules::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(application: MadaApplication)
}
