package com.team.ian.data.repo

import com.team.ian.data.model.Registration

interface UsersRepo {
	suspend fun approveRegistration(email: String)
}