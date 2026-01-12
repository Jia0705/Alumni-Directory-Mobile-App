package com.team.ian.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoURL: String = "",
    val role: Role = Role.NONE
)

enum class Role {
	NONE, ALUMNI, ADMIN
}
