package com.junyan34075453.nutritrack.screen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.junyan34075453.nutritrack.R
import com.junyan34075453.nutritrack.data.NutriTrackViewModel
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(modifier: Modifier = Modifier, onLoginSuccess: (String) -> Unit) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }    // Password check
    var showRegisterSheet by remember { mutableStateOf(false) }     // show the button sheet

    // Control menu extend
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Read data form viewModel
    val viewModel: NutriTrackViewModel = viewModel(
        factory = NutriTrackViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )


    // Use Flow and collectAsState to automatically observe data changes
    val userIds by viewModel.getAllUserIds().collectAsState(initial = emptyList())

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

//        Box {
//            // Background
//            Image(
//                painter = painterResource(id = R.drawable.login_background),
//                contentDescription = "Background",
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
//        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Log in",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive
            )

            // add space between title and username
            Spacer(modifier = Modifier.height(24.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {expanded = !expanded}
            ) {
                OutlinedTextField(
                    // username input field
                    value = userId,
                    onValueChange = { },
                    label = { Text(text = "My ID (Provided by Clinician)") },
                    readOnly = true,
                    placeholder = { Text(text = "Please enter the ID")},
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .menuAnchor(),
                    shape = RoundedCornerShape(15.dp),
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                )

                // Dropdown user id menu
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    userIds.forEach { id ->
                        DropdownMenuItem(
                            text = { Text(id) },
                            onClick = {
                                userId = id
                                expanded = false
                            }
                        )
                    }
                }
            }

            // add space between username and password
            Spacer(modifier = Modifier.height(16.dp))

            // password text field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    loginError = it.isEmpty()
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp),
                isError = loginError,
                singleLine = true
            )

            // check password
            if (loginError) {
                Text(
                    text = "You have not enter your password",
                    color = MaterialTheme.colorScheme.error
                )
            }

            // add space between phoneNumber and text
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This app is only for pre-registered users, Please have your lD and phone number handy before continuing.",
                modifier = Modifier.fillMaxWidth(0.88f),
                fontFamily = FontFamily.SansSerif,
            )

            // add space between text and button
            Spacer(modifier = Modifier.height(16.dp))

            // Button login
            ElevatedButton(
                onClick = {
                    if (userId.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please enter your ID and password", Toast.LENGTH_SHORT).show()
                        return@ElevatedButton
                    }

                    viewModel.viewModelScope.launch {
                        val patient = viewModel.getPatientByIdAndPassword(userId, password)
                        if (patient != null) {
                            // keeping login
                            viewModel.setLoggedInUser(context, userId)
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_LONG).show()
                            onLoginSuccess(userId)
                        } else {
                            Toast.makeText(context, "Incorrect ID or password", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text("Continue")
            }

            // Space between two button
            Spacer(modifier = Modifier.height(12.dp))

            // Button register
            ElevatedButton(
                onClick = { showRegisterSheet = true },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text("Don't have an account? Register")
            }

            if (showRegisterSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showRegisterSheet = false }
                ) {
                    RegisterScreen(
                        onRegisterSuccess = {
                            showRegisterSheet = false
                            // Login information can be automatically filled in
                        },
                        onNavigateToLogin = {
                            showRegisterSheet = false
                            // Can switch to the login interface here
                        }
                    )
                }
            }
        }
    }
}

