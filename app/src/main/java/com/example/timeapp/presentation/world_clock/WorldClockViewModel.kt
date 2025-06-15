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
        _allCityTime.clear()
        allZoneIds.forEach { zoneId ->
            _allCityTime.add(zoneId to worldClockRepository.getCityTime(zoneId))
        }
    }

    private fun updateSelectedCityTime() {
        for (i in _selectedCityTime.indices) {
            val zoneId = _selectedCityTime[i].first
            _selectedCityTime[i] = zoneId to worldClockRepository.getCityTime(zoneId)
        }
    }

    private fun updateAllCityTime() {
        for (i in _allCityTime.indices) {
            val zoneId = _allCityTime[i].first
            _allCityTime[i] = zoneId to worldClockRepository.getCityTime(zoneId)
        }
    }

    fun addCityTime(zoneId: String) {
        val time = worldClockRepository.getCityTime(zoneId)
        _selectedCityTime.add(zoneId to time)
        val idx = _allCityTime.indexOfFirst { it.first == zoneId }
        if (idx >= 0) _allCityTime.removeAt(idx)
    }

    fun deleteCityTime(zoneId: String) {
        val idx = _selectedCityTime.indexOfFirst { it.first == zoneId }
        if (idx >= 0) _selectedCityTime.removeAt(idx)
        val time = worldClockRepository.getCityTime(zoneId)
        if (_allCityTime.none { it.first == zoneId }) {
            _allCityTime.add(zoneId to time)
        }
    }

    fun getCityName(zoneId: String): String = worldClockRepository.getCityName(zoneId)

}