package com.example.speedtest

import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(massage: String): Exception()
}