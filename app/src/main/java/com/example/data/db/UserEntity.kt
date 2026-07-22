package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val phone: String,
    val name: String,
    val password: String,
    val role: String, // "ADMIN" or "CUSTOMER"
    val createdAt: Long = System.currentTimeMillis()
)
