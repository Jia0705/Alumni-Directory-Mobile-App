package com.team.ian.data.model

data class ExtendedInfo(
    val pastJobHistory: List<String> = listOf(""),
    val skills: List<String> = listOf(""),
    val shortBio: String = "",
    val profilePicUrl: String = ""
)