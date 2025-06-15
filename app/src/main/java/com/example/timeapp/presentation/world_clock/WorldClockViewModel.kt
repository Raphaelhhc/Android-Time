package com.example.timeapp.presentation.world_clock

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeapp.domain.WorldClockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

private const val REFRESH_INTERVAL_MS = 16L

@HiltViewModel
class WorldClockViewModel @Inject constructor(
    private val worldClockRepository: WorldClockRepository
): ViewModel() {

    private val _selectedCityTime = mutableStateListOf<Pair<String, LocalTime>>()
    val selectedCityTime get() = _selectedCityTime

    // All city time that not in selected list
    private val _allCityTime = mutableStateListOf<Pair<String, LocalTime>>()
    val allCityTime get() = _allCityTime

    init {
        viewModelScope.launch {
            fetchAllCityTime(worldClockRepository.allZoneIds)
            while (true) {
                updateSelectedCityTime()
                updateAllCityTime()
                delay(REFRESH_INTERVAL_MS)
            }
        }
    }

    private fun fetchAllCityTime(allZoneIds: List<String>) {
        // TODO Make list of all city time from list of all zone ids
    }

    private fun updateSelectedCityTime() {
        // TODO Update local time for cities in selected city time list
    }

    private fun updateAllCityTime() {
        // TODO Update local time for cities in all city time list
    }

    fun addCityTime(zoneId: String) {
        // TODO: add city time in selectedCityTime with zoneId and remove the city from allCityTime
    }

    fun deleteCityTime(zoneId: String) {
        // TODO: delete city time from selectedCityTime with zoneId and add back the city from allCityTime
    }

}