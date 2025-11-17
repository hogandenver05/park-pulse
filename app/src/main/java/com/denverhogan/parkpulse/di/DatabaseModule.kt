package com.denverhogan.parkpulse.di

import android.content.Context
import androidx.room.Room
import com.denverhogan.parkpulse.data.local.FavoriteParkDao
import com.denverhogan.parkpulse.data.local.ParkPulseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideParkPulseDatabase(@ApplicationContext context: Context): ParkPulseDatabase {
        return Room.databaseBuilder(
            context,
            ParkPulseDatabase::class.java,
            "park-pulse-database"
        ).build()
    }

    @Provides
    fun provideFavoriteParkDao(database: ParkPulseDatabase): FavoriteParkDao {
        return database.favoriteParkDao()
    }
}
