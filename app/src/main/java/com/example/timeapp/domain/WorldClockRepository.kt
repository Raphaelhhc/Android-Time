package com.example.timeapp.domain

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class WorldClockRepository @Inject constructor(private val context: Context) {

    lateinit var allZoneIds: List<String>

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            allZoneIds = ZoneId.getAvailableZoneIds().sorted()
        }
    }

    private fun getCityName(zoneId: String): String {
        // TODO: Get city name from zone id
    }

    private fun getCityTime(zoneId: String): LocalTime {
        // TODO: Get city local time from zone id
    }

}