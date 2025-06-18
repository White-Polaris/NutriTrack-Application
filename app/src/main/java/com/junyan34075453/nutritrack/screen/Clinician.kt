package com.junyan34075453.nutritrack.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.ai.client.generativeai.GenerativeModel
import com.junyan34075453.nutritrack.BuildConfig
import com.junyan34075453.nutritrack.api.ClinicianUiState
import com.junyan34075453.nutritrack.api.ClinicianViewModel
import com.junyan34075453.nutritrack.api.UiState
import com.junyan34075453.nutritrack.data.NutriTrackViewModel
import com.junyan34075453.nutritrack.data.Patient
import com.junyan34075453.nutritrack.data.TipsViewModel
import com.junyan34075453.nutritrack.screen.ui.theme.Assignment3Theme
import kotlinx.coroutines.launch

class Clinician : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userId = intent.getStringExtra("userId") ?: "default"
        setContent {
            Assignment3Theme {
                val navController = rememberNavController()
                ClinicianScreen(navController, userId)
            }
        }
    }
}

@Composable
fun ClinicianScreen(navController: NavController, userId: String = "default"){
    var accessKey by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Clinician Login",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Cursive
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = accessKey,
            onValueChange = { accessKey = it },
            label = { Text("Clinician Key") },
            placeholder = { Text("Enter your clinician key") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            onClick = {
                if (accessKey == "dollar-entry-apples") {
                    // Navigate to Clinician content
                    navController.navigate("clinician_content") {
                        launchSingleTop = true
                        restoreState = true
                    }
                } else {
                    errorMessage = "Invalid Clinician Key"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun ClinicianContentScreen(navController: NavController, userId: String = "default") {
    val viewModel: NutriTrackViewModel = viewModel()
    val clinicianViewModel: ClinicianViewModel = viewModel()
    val uiState by clinicianViewModel.uiState.collectAsState()

    val patients by viewModel.getAllPatients().collectAsState(initial = emptyList())

    // Get average scores
    var avgMaleScore by remember { mutableStateOf(0.0) }
    var avgFemaleScore by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        avgMaleScore = viewModel.getAverageMaleScore()
        avgFemaleScore = viewModel.getAverageFemaleScore()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Clinician Dashboard",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Cursive
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Average Scores Section
        Text(
            text = "Average HEIFA Scores",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AverageScoreCard(
                title = "Male",
                score = avgMaleScore,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            AverageScoreCard(
                title = "Female",
                score = avgFemaleScore,
                modifier = Modifier.weight(1f)
            )
        }

        // Divider
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Data Pattern Analysis Section
        Text(
            text = "DATA PATTERN ANALYSIS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Start
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedButton(
                onClick = {
                    viewModel.viewModelScope.launch {
                        clinicianViewModel.analyzePatientData(patients)
                    }
                },
                enabled = uiState !is ClinicianUiState.Loading
            ) {
                Text("Find Data Patterns")
                Icon(Icons.Default.Search, contentDescription = "Analyze data")
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF8F6FD)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                when (uiState) {
                    is ClinicianUiState.Initial -> {
                        PatternPlaceholder("Click button to analyze data patterns")
                    }
                    is ClinicianUiState.Loading -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            repeat(3) {
                                PatternPlaceholder("Analyzing...")
                            }
                        }
                    }
                    is ClinicianUiState.Success -> {
                        val patterns = (uiState as ClinicianUiState.Success).patterns
                        val filledPatterns = patterns + List(3 - patterns.size) { "No data available" }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            filledPatterns.take(3).forEachIndexed { index, pattern ->
                                PatternCard(
                                    index = index + 1,
                                    content = pattern
                                )
                            }
                        }
                    }
                    is ClinicianUiState.Error -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            PatternCard(
                                index = 1,
                                content = (uiState as ClinicianUiState.Error).errorMessage
                            )
                            PatternPlaceholder("Please try again later")
                            PatternPlaceholder("Check API configuration")
                        }
                    }
                }
            }
        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            ElevatedButton(
                onClick = {
                    Screen.navigateTo(navController, Screen.Settings, userId)
                }
            ) {
                Text("Done ")
                Icon(Icons.Default.Done, contentDescription = "Generate message")
            }
        }

    }
}

@Composable
fun AverageScoreCard(title: String, score: Double, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Average HEIFA ($title)",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "%.1f".format(score),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PatternPlaceholder(text: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            color = Color.Gray
        )
    }
}

@Composable
fun PatternCard(index: Int, content: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = when(index) {
                1 -> Color(0xFFE8F5E9)  // Green
                2 -> Color(0xFFE3F2FD)  // Blue
                else -> Color(0xFFF3E5F5) // Purple
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index.",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


