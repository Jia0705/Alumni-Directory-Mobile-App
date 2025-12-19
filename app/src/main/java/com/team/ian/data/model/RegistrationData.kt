package com.team.ian.data.model

data class RegistrationData(
    val name: String = "",
    val email: String = "",
    val pass: String = "",
    val gradYear: String = "",
    val major: String = "",
    val curPosition: String = "",
    val curCompany: String = "",
    val techStack: String = "",
    val curCityandCountry: Pair<String, String> = Pair("",""),
    val contactPref: String = "",
    val contactLinks: List<String> = listOf(""),
)

