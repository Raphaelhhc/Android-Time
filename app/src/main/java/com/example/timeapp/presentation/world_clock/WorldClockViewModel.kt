package com.example.timeapp.presentation.world_clock

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timeapp.domain.WorldClockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
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
            worldClockRepository.getSelectedZoneIds().collectLatest { selected ->
                updateLists(allZoneIds, selected)
            }
        }
    }

    private fun updateLists(allZoneIds: List<String>, selected: List<String>) {
        _selectedCityTime.clear()
        selected.forEach { zoneId ->
            _selectedCityTime.add(zoneId to worldClockRepository.getCityTime(zoneId))
        }

        _allCityTime.clear()
        allZoneIds.filter { it !in selected }.forEach { zoneId ->
            _allCityTime.add(zoneId to worldClockRepository.getCityTime(zoneId))
        }
    }

    fun addCityTime(zoneId: String) {
        viewModelScope.launch {
            worldClockRepository.addCity(zoneId)
        }
    }

    fun deleteCityTime(zoneId: String) {
        viewModelScope.launch {
            worldClockRepository.deleteCity(zoneId)
        }
    }

    fun getCityName(zoneId: String): String = worldClockRepository.getCityName(zoneId)

}