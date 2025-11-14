package com.denverhogan.parkpulse.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteParkDao {
    @Query("SELECT id FROM favorite_parks")
    fun getFavoriteParkIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favoritePark: FavoritePark)

    @Query("DELETE FROM favorite_parks WHERE id = :parkId")
    suspend fun removeFavorite(parkId: Int)
}
