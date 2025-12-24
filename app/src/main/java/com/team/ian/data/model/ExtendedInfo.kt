package com.team.ian.data.model

data class ExtendedInfo(
    val pastJobHistory: List<String> = emptyList(),
    val skills: List<String> = emptyList(),
    val shortBio: String = "",
    val profilePic: String = ""
)
