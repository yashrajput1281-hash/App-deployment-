package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_records")
data class WorkRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val requestId: Long? = null,
    val workTitle: String,
    val customerName: String,
    val customerPhone: String,
    val amountEarned: Double,
    val category: String,
    val completedDate: Long = System.currentTimeMillis(),
    val remarks: String = ""
)
