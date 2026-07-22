package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_requests")
data class ServiceRequestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val customerPhone: String,
    val serviceCategory: String,
    val serviceTitle: String,
    val customerNotes: String,
    val status: String = "Pending", // "Pending", "In Progress", "Completed", "Cancelled"
    val feeAmount: Double = 0.0,
    val adminNotes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)
