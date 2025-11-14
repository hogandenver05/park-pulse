package com.denverhogan.parkpulse.di

import com.denverhogan.parkpulse.network.QueueTimesApi
import com.denverhogan.parkpulse.repository.ParksRepository
import com.denverhogan.parkpulse.repository.RidesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideParksRepository(api: QueueTimesApi): ParksRepository {
        return ParksRepository(api)
    }

    @Provides
    fun provideRidesRepository(api: QueueTimesApi): RidesRepository {
        return RidesRepository(api)
    }
}
