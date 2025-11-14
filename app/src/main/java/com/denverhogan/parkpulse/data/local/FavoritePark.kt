package com.denverhogan.parkpulse.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_parks")
data class FavoritePark(
    @PrimaryKey val id: Int
)
