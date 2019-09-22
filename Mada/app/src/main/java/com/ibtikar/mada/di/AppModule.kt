package com.ibtikar.mada.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Mariam Eskander on 2019-06-05.
 */


@Module(
    includes = [ViewModelModule::class, RepositoriesModule::class,
        NetworkModule::class]
)
class AppModule