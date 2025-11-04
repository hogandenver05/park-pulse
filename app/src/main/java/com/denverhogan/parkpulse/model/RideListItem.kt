package com.denverhogan.parkpulse.model

data class RideListItem(
    val id: Int,
    val name: String,
    val isOpen: Boolean,
    val waitTime: Int,
    val lastUpdated: String
)