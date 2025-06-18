package com.junyan34075453.nutritrack.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.junyan34075453.nutritrack.data.NutriTrackViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateToLogin: () -> Unit, modifier: Modifier = Modifier) {
    var userId by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    // error check
    var expanded by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var phoneNumberError by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val viewModel: NutriTrackViewModel = viewModel(
        factory = NutriTrackViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )

    // Get all user ID
    val userIds by viewModel.getAllUserIds().collectAsState(initial = emptyList())

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Text(
                text = "Register",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive
            )

            // user id drop down button
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = userId,
                    onValueChange = {},
                    label = { Text("My ID (Provided by Clinician)") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .menuAnchor(),
                    shape = RoundedCornerShape(15.dp),
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
                )
                // for each to get all user id
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

            // phone number text field
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it;
                    phoneNumberError = it.length < 11
                },
                label = { Text("Phone Number (for verification)") },
                placeholder = { Text(text = "Please enter your phone number")},
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneNumberError,
                singleLine = true
            )
            // phone error check
            if (phoneNumberError) {
                Text(
                    text = "Phone must be at least 11 numbers",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // name text field
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it;
                    nameError = it.isEmpty()
                },
                label = { Text("Your Full Name") },
                placeholder = { Text(text = "Please enter your name")},
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp),
                isError = nameError,
                singleLine = true
            )
            // name error check
            if (nameError) {
                Text(
                    text = "Name is required",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // password text field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it;
                    passwordError = it.isEmpty() || it.length < 6
                },
                label = { Text("Password") },
                placeholder = { Text(text = "Please enter your password")},
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError,
                singleLine = true
            )
            // password error check
            if (passwordError) {
                Text(
                    text = "Password must be at least 6 characters",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // confirm password text field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it;
                    confirmPasswordError = password != confirmPassword
                },
                label = { Text("Confirm Password") },
                placeholder = { Text(text = "Please enter your password again")},
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = confirmPasswordError,
                singleLine = true
            )
            // confirm password error check
            if (confirmPasswordError) {
                Text(
                    text = "Passwords do not match",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Text(
                text = "This app is only for pre-registered users. Please enter your ID, phone number and password to claim your account.",
                modifier = Modifier.fillMaxWidth(0.88f),
                fontFamily = FontFamily.SansSerif
            )

            // Verify and Register
            ElevatedButton(
                onClick = {
                    viewModel.viewModelScope.launch {
                        // Verify that the phone number matches
                        val patient = viewModel.getPatientByIdAndPhone(userId, phoneNumber)
                        if (patient == null) {
                            Toast.makeText(context, "Something is wrong!", Toast.LENGTH_LONG).show()
                        } else {
                            val updatedPatient = patient.copy(
                                password = password,
                                name = name
                            )
                            viewModel.updatePatient(updatedPatient)
                            Toast.makeText(context, "Registration complete!", Toast.LENGTH_SHORT).show()
                            onRegisterSuccess()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text("Register")
            }

            ElevatedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text("Already have an account? Login")
            }
        }

    }
}