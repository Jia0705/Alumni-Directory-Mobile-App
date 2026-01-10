package com.team.ian.data.model

import com.google.firebase.database.PropertyName

data class ExtendedInfo(
	val uid: String = "",
//    @PropertyName("pastJobHistory")
    val pastJobHistory: List<String> = emptyList(),
    val skills: List<String> = emptyList(),
//    @PropertyName("shortBio")
    val shortBio: String = "",
//    @PropertyName("profilePicUrl")
    val profilePicUrl: String = ""
)
