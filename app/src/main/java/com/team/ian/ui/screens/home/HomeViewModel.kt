package com.team.ian.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.core.utils.AlumniFilter
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.Role
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val alumniRepo: AlumniRepo,
	private val authService: AuthService
): ViewModel() {
	private val _allAlumni = MutableStateFlow(emptyList<Alumni>())
	private val _search = MutableStateFlow("")
	val search = _search.asStateFlow()

	private val _selectedStack = MutableStateFlow<String?>(null)
	val selectedStack = _selectedStack.asStateFlow()

	private val _selectedCountry = MutableStateFlow<String?>(null)
	val selectedCountry = _selectedCountry.asStateFlow()

	private val _selectedYear = MutableStateFlow<Int?>(null)
	val selectedYear = _selectedYear.asStateFlow()

	private val _alumni = MutableStateFlow(emptyList<Alumni>())
	val alumni = _alumni.asStateFlow()

	private val _availableStacks = MutableStateFlow<List<String>>(emptyList())
	val availableStacks = _availableStacks.asStateFlow()

	private val _availableCountries = MutableStateFlow<List<String>>(emptyList())
	val availableCountries = _availableCountries.asStateFlow()

	private val _availableYears = MutableStateFlow<List<Int>>(emptyList())
	val availableYears = _availableYears.asStateFlow()

	init {
		getAllAlumni()
		observeFilters()
	}

	fun getAllAlumni(){
		viewModelScope.launch (Dispatchers.IO) {
			try{
				val currentUserId = authService.getCurrentUser()?.id
				alumniRepo.getApprovedAlumni().collect { allAlumni ->
					// Filter out current user's profile
					val filtered = allAlumni.filter { it.uid != currentUserId && it.role != Role.ADMIN }
					_allAlumni.value = filtered
					
					// Update filters
					_availableStacks.value = filtered
						.map { it.primaryStack }
						.filter { it.isNotBlank() }
						.distinct()  // Remove duplicates (means only show one)
						.sorted()
					
					_availableCountries.value = filtered
						.map { it.country }
						.filter { it.isNotBlank() }
						.distinct()
						.sorted()
					
					_availableYears.value = filtered
						.map { it.graduationYear }
						.filter { it > 0 }
						.distinct()
						.sortedDescending()
				}
			}catch (e: Exception){
				Log.d("debugging",e.toString())
			}
		}
	}

	fun updateSearch(query: String) {
		_search.value = query
	}

	fun updateStackFilter(stack: String?) {
		_selectedStack.value = stack
	}

	fun updateCountryFilter(country: String?) {
		_selectedCountry.value = country
	}

	fun updateYearFilter(year: Int?) {
		_selectedYear.value = year
	}

	fun clearAllFilters() {
		_search.value = ""
		_selectedStack.value = null
		_selectedCountry.value = null
		_selectedYear.value = null
	}

	private fun observeFilters() {
		viewModelScope.launch {
			combine(
				_allAlumni,
				_search,
				_selectedStack,
				_selectedCountry,
				_selectedYear
			) { allAlumni, query, stack, country, year ->
				AlumniFilter.filterAlumni(
					alumniList = allAlumni,
					query = query,
					stack = stack,
					country = country,
					year = year
				)
			}.collect { filteredList ->
				_alumni.value = filteredList
			}
		}
	}
}