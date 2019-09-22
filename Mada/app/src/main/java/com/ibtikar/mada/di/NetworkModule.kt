package com.ibtikar.mada.di

import com.ibtikar.mada.BuildConfig
import com.ibtikar.mada.remote.AccountServices
import com.ibtikar.mada.remote.httpErrors.RxErrorHandlingCallAdapterFactory

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Mariam Eskander on 2019-05-14.
 */

@Module
class NetworkModule {

    @Singleton
    @Provides
    internal fun provideRetrofitInterface(
            adapterFactory: RxErrorHandlingCallAdapterFactory,
            okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://sbpaymentservices.payfort.com/FortAPI/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(adapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideRxErrorHandlingCallAdapterFactory(): RxErrorHandlingCallAdapterFactory {
        return RxErrorHandlingCallAdapterFactory.create()
    }

    @Singleton
    @Provides
    internal fun provideOkHttp(): OkHttpClient {

        val logging = HttpLoggingInterceptor()

        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE


        return OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()

                val request = original.newBuilder()
                        .build()

                it.proceed(request)
            }
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(logging)
                .cache(null).build()
    }

    @Singleton
    @Provides
    fun provideAccountServicesRetrofit(
        adapterFactory: RxErrorHandlingCallAdapterFactory,
        okHttpClient: OkHttpClient
    ): AccountServices {
        return Retrofit.Builder()
            .baseUrl("https://sbpaymentservices.payfort.com/FortAPI/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(adapterFactory)
            .client(okHttpClient)
            .build()
            .create(AccountServices::class.java)
    }



}