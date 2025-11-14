package com.denverhogan.parkpulse.model

data class ParentCompany(
    val id: Int,
    val name: String,
    val parks: List<Park>
)

data class Park(
    val id: Int,
    val name: String,
    val country: String,
    val continent: String,
    val latitude: String,
    val longitude: String,
    val timezone: String
)
