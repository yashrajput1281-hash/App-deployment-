package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.ServiceRequestEntity
import com.example.data.db.UserEntity
import com.example.data.db.WorkRecordEntity
import com.example.data.repository.CscRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class CustomerLoggedIn(val user: UserEntity) : AuthState()
    object AdminLoggedIn : AuthState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CscRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = CscRepository(
            userDao = db.userDao(),
            serviceRequestDao = db.serviceRequestDao(),
            workRecordDao = db.workRecordDao()
        )

        // Seed initial data if needed
        viewModelScope.launch {
            seedInitialData()
        }
    }

    // Auth state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // UI Toast/Message state
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun clearToastMessage() {
        _toastMessage.value = null
    }

    // Requests Flows
    val allRequests: StateFlow<List<ServiceRequestEntity>> = repository.getAllRequests()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val customerRequests: StateFlow<List<ServiceRequestEntity>> = authState.flatMapLatest { state ->
        if (state is AuthState.CustomerLoggedIn) {
            repository.getRequestsByCustomerPhone(state.user.phone)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Works & Revenue Flows
    val allWorks: StateFlow<List<WorkRecordEntity>> = repository.getAllWorks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalEarnings: StateFlow<Double> = repository.getTotalEarnings()
        .combine(flowOf(0.0)) { total, _ -> total ?: 0.0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    // Admin Credentials
    val adminPhone = "8813949423"
    val adminPassword = "Raghav#151115"

    fun loginAdmin(phoneInput: String, passwordInput: String) {
        if (phoneInput.trim() == adminPhone && passwordInput.trim() == adminPassword) {
            _authState.value = AuthState.AdminLoggedIn
            _toastMessage.value = "Admin Login Successful!"
        } else {
            _toastMessage.value = "Invalid Admin Phone or Password!"
        }
    }

    fun loginCustomer(phoneInput: String, passwordInput: String) {
        if (phoneInput.isBlank() || passwordInput.isBlank()) {
            _toastMessage.value = "Please fill all fields!"
            return
        }
        viewModelScope.launch {
            val user = repository.getUserByPhone(phoneInput.trim())
            if (user != null && user.password == passwordInput) {
                _authState.value = AuthState.CustomerLoggedIn(user)
                _toastMessage.value = "Welcome back, ${user.name}!"
            } else {
                _toastMessage.value = "Invalid Phone or Password. Please sign up if new."
            }
        }
    }

    fun registerCustomer(name: String, phone: String, password: String) {
        if (name.isBlank() || phone.isBlank() || password.isBlank()) {
            _toastMessage.value = "Please complete all sign-up fields!"
            return
        }
        viewModelScope.launch {
            val existing = repository.getUserByPhone(phone.trim())
            if (existing != null) {
                _toastMessage.value = "Account already exists with this Phone Number! Please log in."
            } else {
                val newUser = UserEntity(
                    phone = phone.trim(),
                    name = name.trim(),
                    password = password,
                    role = "CUSTOMER"
                )
                repository.registerUser(newUser)
                _authState.value = AuthState.CustomerLoggedIn(newUser)
                _toastMessage.value = "Account Created Successfully! Welcome ${newUser.name}"
            }
        }
    }

    fun logout() {
        _authState.value = AuthState.Unauthenticated
        _toastMessage.value = "Logged out successfully"
    }

    // Service Requests
    fun submitServiceRequest(serviceTitle: String, serviceCategory: String, notes: String) {
        val currentState = _authState.value
        if (currentState !is AuthState.CustomerLoggedIn) {
            _toastMessage.value = "Please log in to submit a service request!"
            return
        }

        viewModelScope.launch {
            val request = ServiceRequestEntity(
                customerName = currentState.user.name,
                customerPhone = currentState.user.phone,
                serviceCategory = serviceCategory,
                serviceTitle = serviceTitle,
                customerNotes = notes.ifBlank { "Standard $serviceTitle Request" },
                status = "Pending"
            )
            repository.submitRequest(request)
            _toastMessage.value = "Service Request Submitted Successfully!"
        }
    }

    fun updateRequestStatus(
        request: ServiceRequestEntity,
        newStatus: String,
        feeAmount: Double = 0.0,
        adminNotes: String = ""
    ) {
        viewModelScope.launch {
            val isCompleting = newStatus == "Completed" && request.status != "Completed"
            val updated = request.copy(
                status = newStatus,
                feeAmount = if (feeAmount > 0) feeAmount else request.feeAmount,
                adminNotes = adminNotes.ifBlank { request.adminNotes },
                completedAt = if (newStatus == "Completed") System.currentTimeMillis() else request.completedAt
            )
            repository.updateRequest(updated)

            // If changing to completed, record work in Works section automatically
            if (isCompleting) {
                val work = WorkRecordEntity(
                    requestId = request.id,
                    workTitle = request.serviceTitle,
                    customerName = request.customerName,
                    customerPhone = request.customerPhone,
                    amountEarned = feeAmount,
                    category = request.serviceCategory,
                    remarks = if (adminNotes.isNotBlank()) adminNotes else "Completed service order #${request.id}"
                )
                repository.recordWork(work)
            }
            _toastMessage.value = "Request status updated to $newStatus!"
        }
    }

    fun recordManualWork(
        workTitle: String,
        customerName: String,
        customerPhone: String,
        amountEarned: Double,
        category: String,
        remarks: String
    ) {
        if (workTitle.isBlank() || amountEarned <= 0.0) {
            _toastMessage.value = "Please provide valid work title and earned amount!"
            return
        }
        viewModelScope.launch {
            val work = WorkRecordEntity(
                workTitle = workTitle.trim(),
                customerName = customerName.ifBlank { "Walk-in Customer" },
                customerPhone = customerPhone.ifBlank { "N/A" },
                amountEarned = amountEarned,
                category = category.ifBlank { "General CSC Work" },
                remarks = remarks.ifBlank { "Direct manual work entry" }
            )
            repository.recordWork(work)
            _toastMessage.value = "Work record added! Earned ₹$amountEarned"
        }
    }

    private suspend fun seedInitialData() {
        // Seed sample customer user if not present, but DO NOT seed fake requests or fake works
        val existingUser = repository.getUserByPhone("9876543210")
        if (existingUser == null) {
            val sampleCustomer = UserEntity(
                phone = "9876543210",
                name = "Ramesh Kumar",
                password = "123",
                role = "CUSTOMER"
            )
            repository.registerUser(sampleCustomer)
        }
    }
}
