package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWork(work: WorkRecordEntity): Long

    @Query("SELECT * FROM work_records ORDER BY completedDate DESC")
    fun getAllWorks(): Flow<List<WorkRecordEntity>>

    @Query("SELECT SUM(amountEarned) FROM work_records")
    fun getTotalEarnings(): Flow<Double?>

    @Query("DELETE FROM work_records WHERE id = :id")
    suspend fun deleteWorkById(id: Long)
}
