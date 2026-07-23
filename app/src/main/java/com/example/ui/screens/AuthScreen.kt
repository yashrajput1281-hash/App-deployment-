package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CscLogo3D
import com.example.ui.theme.CscGold
import com.example.ui.theme.CscNavy

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(
    onAdminLogin: (phone: String, pass: String) -> Unit,
    onCustomerLogin: (phone: String, pass: String) -> Unit,
    onCustomerSignup: (name: String, phone: String, pass: String) -> Unit
) {
    var roleTab by remember { mutableStateOf(0) } // 0: Customer, 1: Admin
    var customerSubTab by remember { mutableStateOf(0) } // 0: Login, 1: Sign Up

    // Customer fields (empty by default, autofill removed)
    var custName by remember { mutableStateOf("") }
    var custPhone by remember { mutableStateOf("") }
    var custPass by remember { mutableStateOf("") }

    // Admin fields (empty by default, autofill removed)
    var adminPhone by remember { mutableStateOf("") }
    var adminPass by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CscNavy,
                        Color(0xFF0F2548),
                        Color(0xFF0B101D)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // Custom 3D CSC Logo from uploaded design
            CscLogo3D(size = 96.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "AKASH CSC & CPLO WORKS",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Common Service Centre & CPLO Citizen Portal",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CscGold,
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 3D Elevated Main Role Switcher
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .shadow(12.dp, RoundedCornerShape(18.dp), spotColor = CscGold)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (roleTab == 0) CscNavy else Color.Transparent)
                            .clickable { roleTab = 0 }
                            .testTag("role_customer_tab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = if (roleTab == 0) CscGold else Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Customer Portal",
                                color = if (roleTab == 0) Color.White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (roleTab == 1) CscGold else Color.Transparent)
                            .clickable { roleTab = 1 }
                            .testTag("role_admin_tab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = if (roleTab == 1) Color.Black else Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Admin Login",
                                color = if (roleTab == 1) Color.Black else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3D Card Container with Smooth Transition Animation
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = CscNavy)
            ) {
                AnimatedContent(
                    targetState = roleTab,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally(animationSpec = tween(300)) { width -> width } + fadeIn()).togetherWith(
                                slideOutHorizontally(animationSpec = tween(300)) { width -> -width } + fadeOut()
                            )
                        } else {
                            (slideInHorizontally(animationSpec = tween(300)) { width -> -width } + fadeIn()).togetherWith(
                                slideOutHorizontally(animationSpec = tween(300)) { width -> width } + fadeOut()
                            )
                        }
                    },
                    label = "RoleTabTransition"
                ) { currentRoleTab ->
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (currentRoleTab == 0) {
                            // Customer Section (Login vs Sign Up)
                            TabRow(
                                selectedTabIndex = customerSubTab,
                                containerColor = Color(0xFFF1F5F9),
                                contentColor = CscNavy,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .fillMaxWidth(),
                                indicator = { tabPositions ->
                                    TabRowDefaults.SecondaryIndicator(
                                        Modifier.tabIndicatorOffset(tabPositions[customerSubTab]),
                                        color = CscNavy,
                                        height = 3.dp
                                    )
                                }
                            ) {
                                Tab(
                                    selected = customerSubTab == 0,
                                    onClick = { customerSubTab = 0 },
                                    modifier = Modifier.testTag("customer_login_subtab")
                                ) {
                                    Text(
                                        text = "Log In",
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = if (customerSubTab == 0) CscNavy else Color.Gray
                                    )
                                }
                                Tab(
                                    selected = customerSubTab == 1,
                                    onClick = { customerSubTab = 1 },
                                    modifier = Modifier.testTag("customer_signup_subtab")
                                ) {
                                    Text(
                                        text = "Sign Up",
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = if (customerSubTab == 1) CscNavy else Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            if (customerSubTab == 1) {
                                // Name field for Sign Up
                                OutlinedTextField(
                                    value = custName,
                                    onValueChange = { custName = it },
                                    label = { Text("Full Name") },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = CscNavy
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("customer_signup_name_input"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CscNavy,
                                        focusedLabelColor = CscNavy
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            // Phone Input
                            OutlinedTextField(
                                value = custPhone,
                                onValueChange = { custPhone = it },
                                label = { Text("Phone Number") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = CscNavy
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("customer_phone_input"),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CscNavy,
                                    focusedLabelColor = CscNavy
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Password Input
                            OutlinedTextField(
                                value = custPass,
                                onValueChange = { custPass = it },
                                label = { Text("Password") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = CscNavy
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle password",
                                            tint = CscNavy
                                        )
                                    }
                                },
                                singleLine = true,
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("customer_password_input"),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CscNavy,
                                    focusedLabelColor = CscNavy
                                )
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (customerSubTab == 0) {
                                        onCustomerLogin(custPhone, custPass)
                                    } else {
                                        onCustomerSignup(custName, custPhone, custPass)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .shadow(6.dp, RoundedCornerShape(14.dp), spotColor = CscNavy)
                                    .testTag("customer_auth_submit_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CscNavy)
                            ) {
                                Text(
                                    text = if (customerSubTab == 0) "LOGIN TO CUSTOMER PORTAL" else "CREATE CUSTOMER ACCOUNT",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                            // AUTOFILL OPTION REMOVED AS REQUESTED

                        } else {
                            // Admin Section
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = CscGold,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Admin Authorization",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CscNavy
                                    )
                                )
                            }

                            Text(
                                text = "Enter Akash CSC admin credentials",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                            )

                            // Admin Phone
                            OutlinedTextField(
                                value = adminPhone,
                                onValueChange = { adminPhone = it },
                                label = { Text("Admin Phone Number") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = CscNavy
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_phone_input"),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CscNavy,
                                    focusedLabelColor = CscNavy
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Admin Password
                            OutlinedTextField(
                                value = adminPass,
                                onValueChange = { adminPass = it },
                                label = { Text("Admin Password") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = CscNavy
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle password",
                                            tint = CscNavy
                                        )
                                    }
                                },
                                singleLine = true,
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_password_input"),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CscNavy,
                                    focusedLabelColor = CscNavy
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { onAdminLogin(adminPhone, adminPass) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .shadow(6.dp, RoundedCornerShape(14.dp), spotColor = CscGold)
                                    .testTag("admin_login_submit_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CscGold)
                            ) {
                                Text(
                                    text = "LOGIN AS ADMIN",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.Black
                                )
                            }
                            // AUTOFILL OPTION REMOVED AS REQUESTED
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                color = Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
            ) {
                Text(
                    text = "Akash CSC Center • Phone: 8813949423 • Government & Citizen Services",
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

