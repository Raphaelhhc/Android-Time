package com.example.timeapp.domain

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class WorldClockRepository @Inject constructor(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val allZoneIdsDeferred = scope.async {
        ZoneId.getAvailableZoneIds().sorted()
    }

    suspend fun getAllZoneIds(): List<String> {
        return allZoneIdsDeferred.await()
    }

    fun getCityName(zoneId: String): String {
        // Get the human readable city name from the given zone id. A zone id
        // typically looks like "Continent/City" or "Continent/Area/City".
        // We simply take the last path segment and replace underscores with
        // spaces so that "Asia/Taipei" becomes "Taipei".
        return zoneId.substringAfterLast('/')
            .replace('_', ' ')
    }

    fun getCityTime(zoneId: String): LocalTime {
        // Return the current local time of the provided zone id.
        // ZonedDateTime.now(zoneId).toLocalTime() is used so the value is
        // independent of the device time zone.
        return ZonedDateTime.now(ZoneId.of(zoneId)).toLocalTime()
    }

}