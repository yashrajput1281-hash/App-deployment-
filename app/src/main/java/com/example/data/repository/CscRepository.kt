package com.example.data.repository

import com.example.data.db.ServiceRequestDao
import com.example.data.db.ServiceRequestEntity
import com.example.data.db.UserDao
import com.example.data.db.UserEntity
import com.example.data.db.WorkRecordDao
import com.example.data.db.WorkRecordEntity
import kotlinx.coroutines.flow.Flow

class CscRepository(
    private val userDao: UserDao,
    private val serviceRequestDao: ServiceRequestDao,
    private val workRecordDao: WorkRecordDao
) {
    // Users
    suspend fun registerUser(user: UserEntity): Long = userDao.insertUser(user)

    suspend fun getUserByPhone(phone: String): UserEntity? = userDao.getUserByPhone(phone)

    // Service Requests
    suspend fun submitRequest(request: ServiceRequestEntity): Long = serviceRequestDao.insertRequest(request)

    suspend fun updateRequest(request: ServiceRequestEntity) = serviceRequestDao.updateRequest(request)

    fun getAllRequests(): Flow<List<ServiceRequestEntity>> = serviceRequestDao.getAllRequests()

    fun getRequestsByCustomerPhone(phone: String): Flow<List<ServiceRequestEntity>> =
        serviceRequestDao.getRequestsByPhone(phone)

    suspend fun getRequestById(id: Long): ServiceRequestEntity? = serviceRequestDao.getRequestById(id)

    suspend fun deleteRequest(id: Long) = serviceRequestDao.deleteRequestById(id)

    // Works & Earnings
    suspend fun recordWork(work: WorkRecordEntity): Long = workRecordDao.insertWork(work)

    fun getAllWorks(): Flow<List<WorkRecordEntity>> = workRecordDao.getAllWorks()

    fun getTotalEarnings(): Flow<Double?> = workRecordDao.getTotalEarnings()

    suspend fun deleteWork(id: Long) = workRecordDao.deleteWorkById(id)
}
