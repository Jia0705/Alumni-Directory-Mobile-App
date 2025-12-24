package com.team.ian.data.model

data class Registration(
	val name: String = "",
	val email: String = "",
	val gradYear: Int = 0,
	val department: String = "",
	val position: String = "",
	val organization: String = "",
	val techStack: String = "",
	val city: String = "",
	val country: String = "",
	val contact: List<String> = emptyList()
)
