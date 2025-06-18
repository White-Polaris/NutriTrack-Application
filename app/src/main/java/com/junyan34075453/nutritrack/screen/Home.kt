package com.junyan34075453.nutritrack.screen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.junyan34075453.nutritrack.R
import com.junyan34075453.nutritrack.data.NutriTrackViewModel
import com.junyan34075453.nutritrack.data.Patient
import com.junyan34075453.nutritrack.screen.ui.theme.Assignment3Theme
import java.io.BufferedReader
import java.io.InputStreamReader

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userId = intent.getStringExtra("userId") ?: "default"
        setContent {
            Assignment3Theme {
                val navController = rememberNavController()
                AppNavigation(navController = navController, userId = userId)
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun HomeScreen(navController: NavController, userId: String = "default") {

    val context = LocalContext.current
    val viewModel: NutriTrackViewModel = viewModel(
        factory = NutriTrackViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )
    // Get patient data
    val patient = remember { mutableStateOf<Patient?>(null) }
    val foodQualityScore = remember { mutableStateOf<String>("0") }

    LaunchedEffect(userId) {
        patient.value = viewModel.getPatientById(userId)
        patient.value?.let { p ->
            // Get score based on gender
            foodQualityScore.value = when (p.sex.trim()) {
                "Male" -> p.heifaTotalScoreMale.toString()
                "Female" -> p.heifaTotalScoreFemale.toString()
                else -> "0"
            }
        }
    }

//    Box {
//        // Background
//        Image(
//            painter = painterResource(id = R.drawable.background),
//            contentDescription = "Background",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Crop
//        )
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Personalized greeting
        Text(
            text = "Hello,",
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Start,
            fontFamily = FontFamily.Cursive
        )

        // Personalized greeting
        Text(
            text = "User " + patient.value?.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            // Info text
            Text(
                text = "You've already filled in your Food Intake Questionnaire, but you can change details here:",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                textAlign = TextAlign.Start,
                fontFamily = FontFamily.Serif,
            )

            // Button to edit questionnaire
            ElevatedButton(
                onClick = {
                    context.startActivity(
                        Intent(context, Questionnaire::class.java).apply {
                            putExtra("userId", userId)
                        }
                    )
                },
                // The button width self-adaption
                modifier = Modifier
                    .wrapContentWidth(),
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(18.dp)
                )
                Text("Edit", modifier = Modifier.padding(start = 4.dp))
            }
        }

        // Title and Logo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Add image at the top
            Image(
                painter = painterResource(id = R.drawable.health_home_logo),
                contentDescription = "NutriTrack Logo",
                modifier = Modifier.size(255.dp) // Adjust size as needed
            )
        }

        // Score section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Score",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            TextButton(
                onClick = {
                    Screen.navigateTo(navController, Screen.Insights, userId)
                }
            ) {
                Text("See all scores")
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "See all",
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = android.R.drawable.star_on),
                contentDescription = "Select time",
            )

            Text(
                text = "  Your Food Quality score",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 24.dp),
                textAlign = TextAlign.Start,
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp
            )

            // Display the food quality score in a prominent way
            Text(
                text = "${foodQualityScore.value}/100",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF388E3C), // Green color for the score
                // The button width self-adaption
                modifier = Modifier
                    .wrapContentWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Explanation section
        Text(
            text = "What is the Food Quality Score?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Start
        )

        Text(
            text = "Your Food Quality Score provides a snapshot of how well your eating patterns align with established food guidelines," +
                    " helping you identify both strengths and opportunities for improvement in your diet.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Start,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "This personalized measurement considers various food groups including vegetables," +
                    " fruits, whole grains, and proteins to give you practical insights for making healthier food choices.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Start,
            fontFamily = FontFamily.Serif
        )
    }
}

// Navigation of the using page
@Composable
fun AppNavigation(navController: NavHostController, userId: String) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, userId) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController, userId) }
            composable(Screen.Insights.route) { InsightsScreen(navController, userId) }
            composable(Screen.NutriCoach.route) { NutriCoachScreen(navController, userId) }
            composable(Screen.Settings.route) { SettingsScreen(navController, userId) }
            composable(Screen.Clinician.route) { ClinicianScreen(navController, userId) }
            composable("clinician_content") { ClinicianContentScreen(navController, userId) }
        }
    }
}

// Helper function to load user data from CSV
fun loadUserData(context: Context, userId: String): Map<String, String>? {
    return try {
        context.assets.open("account.csv").use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                // Read header
                val headers = reader.readLine().split(",")

                // Find matching user
                reader.lineSequence()
                    .firstOrNull { line ->
                        val values = line.split(",")
                        values.getOrNull(1)?.trim() == userId
                    }
                    ?.split(",")
                    ?.let { values ->
                        headers.zip(values).toMap()
                    }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
