package com.team.ian.data.model

data class Alumni(
    val uid: String = "",

    // Access control
    val status: String = "pending",   // pending | approved | rejected | inactive
    val role: String = "none",         // none | alumni | admin

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
    val photoURL: String = "",

    // Metadata
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
