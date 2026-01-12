package com.team.ian.core.utils

import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Alumni

object AlumniFilter {
	fun filterAlumni(
		alumniList: List<Alumni>,
		query: String = "",
		stack: String? = null,
		country: String? = null,
		year: Int? = null,
		status: AccountStatus? = null
	): List<Alumni> {
		var filtered = alumniList

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
			filtered = filtered.filter { alumni ->
				alumni.primaryStack.equals(stack, ignoreCase = true)
			}
		}
		if (country != null) {
			filtered = filtered.filter { alumni ->
				alumni.country.equals(country, ignoreCase = true)
			}
		}
		if (year != null) {
			filtered = filtered.filter { alumni ->
				alumni.graduationYear == year
			}
		}

		if (status != null) {
			filtered = filtered.filter { alumni ->
				alumni.status == status
			}
		}

		return filtered
	}

	fun extractStacks(alumniList: List<Alumni>): List<String> {
		return alumniList
			.map { it.primaryStack }
			.filter { it.isNotBlank() }
			.distinct()
			.sorted()
	}

	fun extractCountries(alumniList: List<Alumni>): List<String> {
		return alumniList
			.map { it.country }
			.filter { it.isNotBlank() }
			.distinct()
			.sorted()
	}

	fun extractYears(alumniList: List<Alumni>): List<Int> {
		return alumniList
			.map { it.graduationYear }
			.filter { it > 0 }
			.distinct()
			.sortedDescending()
	}
}