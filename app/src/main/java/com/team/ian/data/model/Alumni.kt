package com.team.ian.data.model

data class Alumni(
    val uid: String = "",

    // Access control
    val status: AccountStatus = AccountStatus.PENDING,   // pending | approved | rejected | inactive
    val role: Role = Role.NONE,         // none | alumni | admin

    // Basic info
    val fullName: String = "",
    val email: String = "",
    val graduationYear: Int = 0,
    val department: String = "",

    // Professional info
    val jobTitle: String = "",
    val company: String = "",
    val primaryStack: String = "",

    // Location
    val city: String = "",
    val country: String = "",

    // Contact & profile
    val linkedin: String = "",
    val github: String = "",
    val phone: String = "",
    val shortBio: String = "",
    val skills: List<String> = emptyList(),
    val pastJobHistory: List<String> = emptyList(),
    val photoURL: String = "",
    val showEmail: Boolean = true,
	val showPhone: Boolean = true,
	val showLinkedIn: Boolean = true,
    val showGithub: Boolean = true,
    val avatarColor: String = "",

    // FCM token
    val token: String = "",

    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class AccountStatus {
	PENDING, APPROVED, REJECTED, INACTIVE
}
