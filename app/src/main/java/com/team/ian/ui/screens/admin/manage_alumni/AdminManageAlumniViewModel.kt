package com.team.ian.ui.screens.admin.manage_alumni

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.Alumni
import com.team.ian.data.repo.AlumniRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AdminManageAlumniViewModel(
	private val alumniRepo: AlumniRepo = AlumniRepo.getInstance()
) : ViewModel() {
	private val _allAlumni = MutableStateFlow(emptyList<Alumni>())
	val allAlumni = _allAlumni.asStateFlow()
	private val _alumni = MutableStateFlow(emptyList<Alumni>())
	val alumni = _alumni.asStateFlow()

	private val _search = MutableStateFlow("")
	val search = _search.asStateFlow()
	private val _selectedStack = MutableStateFlow<String?>(null)
	val selectedStack = _selectedStack.asStateFlow()
	private val _selectedCountry = MutableStateFlow<String?>(null)
	val selectedCountry = _selectedCountry.asStateFlow()
	private val _selectedYear = MutableStateFlow<Int?>(null)
	val selectedYear = _selectedYear.asStateFlow()

	private val _availableStacks = MutableStateFlow<List<String>>(emptyList())
	val availableStacks = _availableStacks.asStateFlow()
	private val _availableCountries = MutableStateFlow<List<String>>(emptyList())
	val availableCountries = _availableCountries.asStateFlow()
	private val _availableYears = MutableStateFlow<List<Int>>(emptyList())
	val availableYears = _availableYears.asStateFlow()

	init {
		getAllUsers()
	}

	fun getAllUsers() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				alumniRepo.getAllAlumniExceptForPending().collect {
					_alumni.value = it
				}
			} catch (e: Exception) {
				Log.d("debugging", e.toString())
			}
		}
	}

	private fun observerFilters() {
		viewModelScope.launch {
			combine(
				_allAlumni,
				_search,
				_selectedStack,
				_selectedCountry,
				_selectedYear
			) { allAlumni, query, stack, country, year ->
				var filtered = allAlumni
				if (query.isNotBlank()) {
					filtered = filtered.filter { alumni ->
						alumni.fullName.contains(query, ignoreCase = true) ||
								alumni.email.contains(query, ignoreCase = true) ||
								alumni.primaryStack.contains(query, ignoreCase = true) ||
								alumni.company.contains(query, ignoreCase = true) ||
								alumni.city.contains(query, ignoreCase = true) ||
								alumni.country.contains(query, ignoreCase = true)
					}
				}

				if (stack != null) {
					filtered = filtered.filter { it.primaryStack.equals(stack, ignoreCase = true) }
				}

				if(country!=null){
					filtered = filtered.filter { it.country.equals() }
				}
			}
		}
	}
}