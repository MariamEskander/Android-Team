package com.ibtikar.mada.di

import com.ibtikar.mada.base.BaseRepository
import com.ibtikar.mada.ui.MainRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Suppress("unused")
@Module
abstract class RepositoriesModule {

    @Binds
    @Singleton
    abstract fun bindMainRepository(mainRepository: MainRepository): BaseRepository

}




