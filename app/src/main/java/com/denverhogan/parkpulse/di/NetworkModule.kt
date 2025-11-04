package com.denverhogan.parkpulse.di

import com.denverhogan.parkpulse.network.QueueTimesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object NetworkModule {
    private const val BASE_URL = "https://queue-times.com/parks/"

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApi(retrofit: Retrofit): QueueTimesApi {
        return retrofit.create<QueueTimesApi>()
    }
}