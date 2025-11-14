package com.denverhogan.parkpulse.model

import com.google.gson.annotations.SerializedName

data class LandsResponse(
    val lands: List<Land>,
    val rides: List<Ride>
)

data class Land(
    val id: Int,
    val name: String,
    val rides: List<Ride>
)

data class Ride(
    val id: Int,
    val name: String,
    @SerializedName("is_open")
    val isOpen: Boolean,
    @SerializedName("wait_time")
    val waitTime: Int,
    @SerializedName("last_updated")
    val lastUpdated: String
)
