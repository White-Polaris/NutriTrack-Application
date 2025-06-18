package com.junyan34075453.nutritrack.screen

import android.annotation.SuppressLint
import android.app.Application
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
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
import androidx.navigation.compose.rememberNavController
import com.junyan34075453.nutritrack.R
import com.junyan34075453.nutritrack.data.NutriTrackViewModel
import com.junyan34075453.nutritrack.data.Patient
import com.junyan34075453.nutritrack.screen.ui.theme.Assignment3Theme

class Insights : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userId = intent.getStringExtra("userId") ?: "default"
        setContent {
            Assignment3Theme {
                val navController = rememberNavController()
                InsightsScreen(navController, userId)
            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun InsightsScreen(navController: NavController, userId: String = "default"){
    val context = LocalContext.current
    val viewModel: NutriTrackViewModel = viewModel(
        factory = NutriTrackViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )
    // Get patient data
    val patient = remember { mutableStateOf<Patient?>(null) }
    val totalScore = remember { mutableStateOf(0f) }

    LaunchedEffect(userId) {
        patient.value = viewModel.getPatientById(userId)
        patient.value?.let { p ->
            // Get total score based on gender
            totalScore.value = when (p.sex.trim()) {
                "Male" -> p.heifaTotalScoreMale.toFloat()
                "Female" -> p.heifaTotalScoreFemale.toFloat()
                else -> 0f
            }
        }
    }

    // Function to get category score
    fun getCategoryScore(patient: Patient?, categoryKey: String): Float {
        return when (categoryKey) {
            "VegetablesHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.vegetablesHeifaScoreMale.toFloat()
                "Female" -> patient.vegetablesHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "FruitHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.fruitHeifaScoreMale.toFloat()
                "Female" -> patient.fruitHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "GrainsandcerealsHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.grainsAndCerealsHeifaScoreMale.toFloat()
                "Female" -> patient.grainsAndCerealsHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "WholegrainsHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.wholeGrainsHeifaScoreMale.toFloat()
                "Female" -> patient.wholeGrainsHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "MeatandalternativesHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.meatAndAlternativesHeifaScoreMale.toFloat()
                "Female" -> patient.meatAndAlternativesHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "DairyandalternativesHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.dairyAndAlternativesHeifaScoreMale.toFloat()
                "Female" -> patient.dairyAndAlternativesHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "WaterHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.waterHeifaScoreMale.toFloat()
                "Female" -> patient.waterHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "UnsaturatedFatHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.unsaturatedFatHeifaScoreMale.toFloat()
                "Female" -> patient.unsaturatedFatHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "SaturatedFatHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.saturatedFatHeifaScoreMale.toFloat()
                "Female" -> patient.saturatedFatHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "SodiumHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.sodiumHeifaScoreMale.toFloat()
                "Female" -> patient.sodiumHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "SugarHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.sugarHeifaScoreMale.toFloat()
                "Female" -> patient.sugarHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "AlcoholHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.alcoholHeifaScoreMale.toFloat()
                "Female" -> patient.alcoholHeifaScoreFemale.toFloat()
                else -> 0f
            }
            "DiscretionaryHEIFAscore" -> when (patient?.sex?.trim()) {
                "Male" -> patient.discretionaryHeifaScoreMale.toFloat()
                "Female" -> patient.discretionaryHeifaScoreFemale.toFloat()
                else -> 0f
            }
            else -> 0f
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
//                .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Insights: Food Score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category breakdown with specific max values
        val categories = listOf(
            CategoryInfo("Vegetables", "VegetablesHEIFAscore", 10f),
            CategoryInfo("Fruits", "FruitHEIFAscore", 10f),
            CategoryInfo("Grains & Cereals", "GrainsandcerealsHEIFAscore", 10f),
            CategoryInfo("Whole Grains", "WholegrainsHEIFAscore", 10f),
            CategoryInfo("Meat & Alternatives", "MeatandalternativesHEIFAscore", 10f),
            CategoryInfo("Dairy", "DairyandalternativesHEIFAscore", 10f),
            CategoryInfo("Water", "WaterHEIFAscore", 5f),
            CategoryInfo("Unsaturated Fats", "UnsaturatedFatHEIFAscore", 10f),
            CategoryInfo("Saturated Fats", "SaturatedFatHEIFAscore", 10f),//
            CategoryInfo("Sodium", "SodiumHEIFAscore", 10f),
            CategoryInfo("Sugar", "SugarHEIFAscore", 10f),
            CategoryInfo("Alcohol", "AlcoholHEIFAscore", 5f),
            CategoryInfo("Discretionary Foods", "DiscretionaryHEIFAscore", 10f)
        )

        categories.forEach { category ->
            val score = getCategoryScore(patient.value, category.csvKeyPrefix)
            CategoryProgress(
                categoryName = category.displayName,
                currentScore = score,
                maxScore = category.maxValue,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = "Total Food Quality Score",
                fontFamily = FontFamily.SansSerif,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
        }

        // Slider in the middle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Slider(
                value = totalScore.value,
                onValueChange = { /* Read-only */ },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF2A4074),
                    activeTrackColor = Color(0xFF6B7FBE)
                ),
                valueRange = 0f..100f,
                modifier = Modifier
                    .width(270.dp)
            )

            Text(
                text = "%.2f/100".format(totalScore.value), // Display with two decimal place,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF388E3C),
                textAlign = TextAlign.End,
            )
        }


        // Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedButton(
                onClick = {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "My NutriTrack Food Quality Score is ${"%.1f".format(totalScore.value)}/100!")
                        type = "text/plain"     // Key of statement
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share text via"))    // Bottom sheet model
                },
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share"
                )
                Text("Share with someone")
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedButton(
                onClick = {
                    Screen.navigateTo(navController, Screen.NutriCoach, userId)
                },
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Improve"
                )
                Text("Improve my diet!")
            }
        }
    }
}


data class CategoryInfo(
    val displayName: String,
    val csvKeyPrefix: String,
    val maxValue: Float
)

@SuppressLint("RememberReturnType")
@Composable
fun CategoryProgress(
    categoryName: String,
    currentScore: Float,
    maxScore: Float,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = categoryName,
                fontSize = 14.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.width(140.dp)
            )

            // Slider
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Slider(
                    value = currentScore,   // Using currentScore in room database
                    onValueChange = { /* Read-only */ },
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF2A4074),
                        activeTrackColor = Color(0xFF6B7FBE)
                    ),
                    valueRange = 0f..maxScore,
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Text(
                text = "%.2f/%.0f".format(currentScore, maxScore), // Show two decimal place for current score
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.width(65.dp),
                textAlign = TextAlign.End
            )
        }

    }
}