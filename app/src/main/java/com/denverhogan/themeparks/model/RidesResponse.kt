package com.denverhogan.themeparks.model

data class LandsResponse(
    val lands: List<Category>,
)

data class Category(
    val id: Int,
    val name: String,
    val rides: List<Ride>
)

data class Ride(
    val id: Int,
    val name: String,
    val isOpen: Boolean,
    val waitTime: Int,
    val lastUpdated: String
)