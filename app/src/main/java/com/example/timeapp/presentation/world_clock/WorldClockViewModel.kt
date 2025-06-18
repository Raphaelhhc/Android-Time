package com.example.timeapp.presentation.world_clock

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeapp.domain.WorldClockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

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
            val allZoneIds = worldClockRepository.getAllZoneIds()
            fetchAllCityTime(allZoneIds)
        }
    }

    private fun fetchAllCityTime(allZoneIds: List<String>) {
        _allCityTime.clear()
        allZoneIds.forEach { zoneId ->
            _allCityTime.add(zoneId to worldClockRepository.getCityTime(zoneId))
        }
    }

    fun addCityTime(zoneId: String) {
        val time = worldClockRepository.getCityTime(zoneId)
        _selectedCityTime.add(zoneId to time)
        val idx = _allCityTime.indexOfFirst { it.first == zoneId }
        if (idx >= 0) _allCityTime.removeAt(idx)
        Log.d("VM", "${_selectedCityTime.toList()}")
    }

    fun deleteCityTime(zoneId: String) {
        val idx = _selectedCityTime.indexOfFirst { it.first == zoneId }
        if (idx >= 0) _selectedCityTime.removeAt(idx)
        val time = worldClockRepository.getCityTime(zoneId)
        if (_allCityTime.none { it.first == zoneId }) {
            _allCityTime.add(zoneId to time)
            _allCityTime.sortBy { it.first }
        }
    }

    fun getCityName(zoneId: String): String = worldClockRepository.getCityName(zoneId)

}