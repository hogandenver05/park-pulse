package com.denverhogan.parkpulse.network

import com.denverhogan.parkpulse.model.Park

sealed interface GetAllParksResult {
    data class Success(val parks: List<Park>) : GetAllParksResult
    data class Error(val message: String) : GetAllParksResult
}
