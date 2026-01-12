package com.team.ian.ui.screens.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.ian.data.model.AccountStatus
import com.team.ian.data.model.Alumni
import com.team.ian.data.model.Role
import com.team.ian.data.repo.AlumniRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminDashboardState(
	val approvedCount: Int = 0,
	val pendingCount: Int = 0,
	val rejectedCount: Int = 0,
	val inactiveCount: Int = 0,
	val totalCount: Int = 0,
	val recentApprovals: List<Alumni> = emptyList()
)

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
	private val alumniRepo: AlumniRepo
) : ViewModel() {
	private val _state = MutableStateFlow(AdminDashboardState())
	val state = _state.asStateFlow()

	init {
		observeMetrics()
	}

	private fun observeMetrics() {
		viewModelScope.launch {
			alumniRepo.getAllUsers().collect { allUsers ->
				val approved = allUsers.filter {
					it.status == AccountStatus.APPROVED && it.role != Role.ADMIN
				}
				val pending = allUsers.filter { it.status == AccountStatus.PENDING }
				val rejected = allUsers.filter { it.status == AccountStatus.REJECTED }
				val inactive = allUsers.filter { it.status == AccountStatus.INACTIVE }
				val recent = approved
					.sortedByDescending { it.updatedAt }
					.take(5)
				_state.value = AdminDashboardState(
					approvedCount = approved.size,
					pendingCount = pending.size,
					rejectedCount = rejected.size,
					inactiveCount = inactive.size,
					totalCount = allUsers.size,
					recentApprovals = recent
				)
			}
		}
	}
}
