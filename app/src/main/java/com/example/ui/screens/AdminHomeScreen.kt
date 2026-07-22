package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ServiceRequestEntity
import com.example.data.db.WorkRecordEntity
import com.example.ui.theme.CscGold
import com.example.ui.theme.CscGreen
import com.example.ui.theme.CscNavy
import com.example.ui.theme.CscRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    allRequests: List<ServiceRequestEntity>,
    allWorks: List<WorkRecordEntity>,
    totalEarnings: Double,
    onLogout: () -> Unit,
    onUpdateRequestStatus: (request: ServiceRequestEntity, status: String, fee: Double, notes: String) -> Unit,
    onRecordManualWork: (title: String, customer: String, phone: String, fee: Double, category: String, remarks: String) -> Unit
) {
    var adminTab by remember { mutableStateOf(0) } // 0: Raised Customer Services, 1: 'Works' & Money Earned
    var statusFilter by remember { mutableStateOf("All") } // "All", "Pending", "In Progress", "Completed"
    var searchQuery by remember { mutableStateOf("") }

    var requestToUpdate by remember { mutableStateOf<ServiceRequestEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CscGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = CscNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "ADMIN DASHBOARD",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Akash CSC • Admin Phone: 8813949423",
                                style = MaterialTheme.typography.bodySmall.copy(color = CscGold)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onLogout, modifier = Modifier.testTag("admin_logout_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CscNavy)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CscNavy,
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = adminTab == 0,
                    onClick = { adminTab = 0 },
                    icon = { Icon(Icons.Default.AssignmentTurnedIn, contentDescription = "Raised Services") },
                    label = { Text("Raised Services (${allRequests.size})") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CscNavy,
                        selectedTextColor = CscGold,
                        indicatorColor = CscGold,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("admin_tab_raised_services")
                )
                NavigationBarItem(
                    selected = adminTab == 1,
                    onClick = { adminTab = 1 },
                    icon = { Icon(Icons.Default.Work, contentDescription = "Works & Revenue") },
                    label = { Text("Works & Earnings") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CscNavy,
                        selectedTextColor = CscGold,
                        indicatorColor = CscGold,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("admin_tab_works")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F5F9))
        ) {
            if (adminTab == 0) {
                // Section 1: Customer Raised Service Requests
                Column(modifier = Modifier.fillMaxSize()) {
                    // Search & Filter Header
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CscNavy)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search by customer name, phone, or service") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("admin_search_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = CscGold,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Status Filter Chips
                        val filters = listOf("All", "Pending", "In Progress", "Completed", "Cancelled")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(filters) { f ->
                                val selected = statusFilter == f
                                FilterChip(
                                    selected = selected,
                                    onClick = { statusFilter = f },
                                    label = { Text(f, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CscGold,
                                        selectedLabelColor = Color.Black,
                                        containerColor = Color(0xFF1E3A8A),
                                        labelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // Request List
                    val filteredRequests = remember(allRequests, searchQuery, statusFilter) {
                        allRequests.filter { req ->
                            val matchesFilter = statusFilter == "All" || req.status.equals(statusFilter, ignoreCase = true)
                            val matchesSearch = searchQuery.isBlank() ||
                                    req.customerName.contains(searchQuery, ignoreCase = true) ||
                                    req.customerPhone.contains(searchQuery, ignoreCase = true) ||
                                    req.serviceTitle.contains(searchQuery, ignoreCase = true)
                            matchesFilter && matchesSearch
                        }
                    }

                    if (filteredRequests.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No raised service requests found matching criteria.",
                                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredRequests) { req ->
                                AdminRequestCard(
                                    request = req,
                                    onUpdateClick = { requestToUpdate = req }
                                )
                            }
                        }
                    }
                }
            } else {
                // Section 2: 'Works' Section & Earnings Analysis
                AdminWorksScreen(
                    allWorks = allWorks,
                    totalEarnings = totalEarnings,
                    onRecordManualWork = onRecordManualWork
                )
            }
        }
    }

    // Status Update Dialog
    requestToUpdate?.let { req ->
        var selectedStatus by remember { mutableStateOf(req.status) }
        var feeInput by remember { mutableStateOf(if (req.feeAmount > 0) req.feeAmount.toString() else "") }
        var adminNotesInput by remember { mutableStateOf(req.adminNotes) }

        AlertDialog(
            onDismissRequest = { requestToUpdate = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = CscNavy,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(text = "Update Service Request #${req.id}", fontWeight = FontWeight.Bold, color = CscNavy)
            },
            text = {
                Column {
                    Text(
                        text = "Customer: ${req.customerName} (${req.customerPhone})",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Service: ${req.serviceTitle}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = CscNavy, fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Select New Status:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))

                    val statuses = listOf("Pending", "In Progress", "Completed", "Cancelled")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        statuses.forEach { st ->
                            val isSel = selectedStatus == st
                            val btnColor = when (st) {
                                "Completed" -> CscGreen
                                "In Progress" -> CscNavy
                                "Cancelled" -> CscRed
                                else -> CscGold
                            }
                            Surface(
                                color = if (isSel) btnColor else Color.LightGray.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedStatus = st }
                            ) {
                                Text(
                                    text = st,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else Color.Black,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = feeInput,
                        onValueChange = { feeInput = it },
                        label = { Text("Fee Charged (₹)") },
                        placeholder = { Text("e.g., 100") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_fee_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = adminNotesInput,
                        onValueChange = { adminNotesInput = it },
                        label = { Text("Admin Remarks / Work Details") },
                        placeholder = { Text("e.g. Document printed, PNR generated, etc.") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .testTag("admin_notes_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val fee = feeInput.toDoubleOrNull() ?: 0.0
                        onUpdateRequestStatus(req, selectedStatus, fee, adminNotesInput)
                        requestToUpdate = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CscNavy),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("confirm_status_update_btn")
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { requestToUpdate = null },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminRequestCard(
    request: ServiceRequestEntity,
    onUpdateClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
    val dateStr = remember(request.createdAt) { dateFormat.format(Date(request.createdAt)) }

    val (statusBg, statusFg, statusIcon) = when (request.status) {
        "Completed" -> Triple(CscGreen.copy(alpha = 0.15f), CscGreen, Icons.Default.CheckCircle)
        "In Progress" -> Triple(CscNavy.copy(alpha = 0.15f), CscNavy, Icons.Default.Schedule)
        "Cancelled" -> Triple(CscRed.copy(alpha = 0.15f), CscRed, Icons.Default.Error)
        else -> Triple(CscGold.copy(alpha = 0.2f), CscGold, Icons.Default.HourglassEmpty)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_request_card_${request.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = request.customerName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = CscNavy,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = request.customerPhone,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = CscNavy
                            )
                        )
                    }
                }

                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = statusFg,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = request.status,
                            color = statusFg,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = request.serviceTitle,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = CscNavy
                        )
                    )
                    Text(
                        text = "Notes: ${request.customerNotes}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
                    )
                    Text(
                        text = "Submitted at: $dateStr",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            if (request.adminNotes.isNotBlank() || request.feeAmount > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (request.adminNotes.isNotBlank()) "Remark: ${request.adminNotes}" else "",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray),
                        modifier = Modifier.weight(1f)
                    )
                    if (request.feeAmount > 0) {
                        Text(
                            text = "Earned: ₹${request.feeAmount}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CscGreen
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onUpdateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("update_status_btn_${request.id}"),
                colors = ButtonDefaults.buttonColors(containerColor = CscNavy),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Update Status & Log Earnings", fontWeight = FontWeight.Bold)
            }
        }
    }
}
