package com.example.bloodlink.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val phoneNumber: String,
    val bloodGroup: String,
    val city: String
)