package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.AdminHomeScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CustomerHomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AuthState
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authState by viewModel.authState.collectAsStateWithLifecycle()
                    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

                    val allRequests by viewModel.allRequests.collectAsStateWithLifecycle()
                    val customerRequests by viewModel.customerRequests.collectAsStateWithLifecycle()
                    val allWorks by viewModel.allWorks.collectAsStateWithLifecycle()
                    val totalEarnings by viewModel.totalEarnings.collectAsStateWithLifecycle()

                    // Toast message trigger
                    LaunchedEffect(toastMessage) {
                        toastMessage?.let { msg ->
                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                            viewModel.clearToastMessage()
                        }
                    }

                    when (val state = authState) {
                        is AuthState.Unauthenticated -> {
                            AuthScreen(
                                onAdminLogin = { phone, pass -> viewModel.loginAdmin(phone, pass) },
                                onCustomerLogin = { phone, pass -> viewModel.loginCustomer(phone, pass) },
                                onCustomerSignup = { name, phone, pass -> viewModel.registerCustomer(name, phone, pass) }
                            )
                        }

                        is AuthState.CustomerLoggedIn -> {
                            CustomerHomeScreen(
                                user = state.user,
                                requests = customerRequests,
                                onLogout = { viewModel.logout() },
                                onSubmitRequest = { title, cat, notes ->
                                    viewModel.submitServiceRequest(title, cat, notes)
                                }
                            )
                        }

                        is AuthState.AdminLoggedIn -> {
                            AdminHomeScreen(
                                allRequests = allRequests,
                                allWorks = allWorks,
                                totalEarnings = totalEarnings,
                                onLogout = { viewModel.logout() },
                                onUpdateRequestStatus = { req, status, fee, notes ->
                                    viewModel.updateRequestStatus(req, status, fee, notes)
                                },
                                onRecordManualWork = { title, customer, phone, fee, category, remarks ->
                                    viewModel.recordManualWork(title, customer, phone, fee, category, remarks)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
