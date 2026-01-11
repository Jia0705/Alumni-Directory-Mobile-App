package com.team.ian.ui.screens.admin.manage_alumni

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.core.utils.AlumniFilter
import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Alumni
import com.team.ian.data.repo.AlumniRepo
import com.team.ian.service.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AdminManageAlumniViewModel(
	private val alumniRepo: AlumniRepo = AlumniRepo.getInstance(),
	private val authService: AuthService = AuthService.getInstance()
) : ViewModel() {
	private val _allAlumni = MutableStateFlow(emptyList<Alumni>())
	private val allAlumni = _allAlumni.asStateFlow()

	private val _search = MutableStateFlow("")
	val search = _search.asStateFlow()
	private val _selectedStatus = MutableStateFlow<AccountStatus?>(null)
	val selectedStatus = _selectedStatus.asStateFlow()

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

	companion object {
		var presetStatus: AccountStatus? = null
	}

	init {
		getAllAlumni()
		observeFilters()
	}

	fun getAllAlumni() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val currentUserId = authService.getCurrentUser()?.id
				alumniRepo.getAllUsers().collect { allUsers ->
					val filtered = allUsers.filter { 
						it.status != AccountStatus.PENDING && it.uid != currentUserId
					}
					_allAlumni.value = filtered
					_availableStacks.value = AlumniFilter.extractStacks(filtered)
					_availableCountries.value = AlumniFilter.extractCountries(filtered)
					_availableYears.value = AlumniFilter.extractYears(filtered)
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}

	fun updateSearch(query: String) {
		_search.value = query
	}

	fun updateStatusFilter(status: AccountStatus?) {
		_selectedStatus.value = status
	}

	fun updateStackFilter(stack: String?) {
		_selectedStack.value = stack
	}

	fun updateCountryFilter(country: String?) {
		_selectedCountry.value = country
	}

	fun updatedYearFilter(year: Int?) {
		_selectedYear.value = year
	}

	fun clearAllFilters() {
		_search.value = ""
		_selectedStack.value = null
		_selectedCountry.value = null
		_selectedYear.value = null
		_selectedStatus.value = null
	}

	private fun observeFilters() {
		viewModelScope.launch {
			combine(
				_allAlumni,
				_search,
				_selectedStack,
				_selectedCountry,
				_selectedYear,
				_selectedStatus
			) { values: Array<Any?> ->
				val allAlumni = values[0] as List<Alumni>
				val query = values[1] as String
				val stack = values[2] as String?
				val country = values[3] as String?
				val year = values[4] as Int?
				val status = values[5] as AccountStatus?

				AlumniFilter.filterAlumni(
					allAlumni, query, stack, country, year, status
				)

			}.collect { filteredList ->
				_alumni.value = filteredList.sortedByDescending { it.createdAt }
			}
		}
	}
}