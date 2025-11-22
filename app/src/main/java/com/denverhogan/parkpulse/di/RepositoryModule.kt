package com.denverhogan.parkpulse.di

import com.denverhogan.parkpulse.data.ParkRepository
import com.denverhogan.parkpulse.data.local.FavoriteParkDao
import com.denverhogan.parkpulse.location.LocationProvider
import com.denverhogan.parkpulse.network.QueueTimesApi
import com.denverhogan.parkpulse.repository.RidesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideParkRepository(
        api: QueueTimesApi,
        favoriteParkDao: FavoriteParkDao,
        locationProvider: LocationProvider
    ): ParkRepository {
        return ParkRepository(api, favoriteParkDao, locationProvider)
    }

    @Provides
    fun provideRidesRepository(api: QueueTimesApi): RidesRepository {
        return RidesRepository(api)
    }
}
