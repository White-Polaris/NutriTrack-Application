package com.junyan34075453.nutritrack.screen

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.junyan34075453.nutritrack.api.NutriCoachViewModel
import com.junyan34075453.nutritrack.api.UiState
import com.junyan34075453.nutritrack.data.NutriCoachTip
import com.junyan34075453.nutritrack.data.NutriTrackViewModel
import com.junyan34075453.nutritrack.data.Patient
import com.junyan34075453.nutritrack.data.TipsViewModel
import com.junyan34075453.nutritrack.screen.ui.theme.Assignment3Theme
import org.json.JSONObject
import java.net.URL


class NutriCoach : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userId = intent.getStringExtra("userId") ?: "default"
        setContent {
            Assignment3Theme {
                val navController = rememberNavController()
                NutriCoachScreen(navController, userId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutriCoachScreen(navController: NavController, userId: String = "default") {
    // fruit
    var fruitName by remember { mutableStateOf("") }
    var fruitData by remember { mutableStateOf<JSONObject?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // ai
    val genAiViewModel: NutriCoachViewModel = viewModel()
    val uiState by genAiViewModel.uiState.collectAsState()
    var showAllTips by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel: NutriTrackViewModel = viewModel(
        factory = NutriTrackViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )

    // Get patient data to check fruit score
    val patient = remember { mutableStateOf<Patient?>(null) }

    LaunchedEffect(userId) {
        patient.value = viewModel.getPatientById(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "NutrCoach",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Cursive
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Check if fruit score is optimal
        val showFruitSearch = patient.value?.let { p ->
            when (p.sex.trim()) {
                "Male" -> p.fruitHeifaScoreMale < 2
                "Female" -> p.fruitHeifaScoreFemale < 2
                else -> true
            }
        } ?: true

        // Fruit search Section
        Text(
            text = "FRUIT DETAIL",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Start
        )

        if (showFruitSearch) {
            // Search section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                OutlinedTextField(
                    value = fruitName,
                    onValueChange = { fruitName = it },
                    label = { Text("Enter fruit name") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(15.dp),
                    singleLine = true
                )

                ElevatedButton(
                    onClick = {
                        if (fruitName.isNotBlank()) {
                            isLoading = true
                            errorMessage = null
                            fetchFruitData(fruitName) { result, error ->
                                isLoading = false
                                if (error != null) {
                                    errorMessage = error
                                } else {
                                    fruitData = result
                                }
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }

            if (isLoading) {
                Text("Loading fruit data...")
            }

            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                // Nutrition rows
                NutritionRow(
                    label = "family",
                    value = fruitData?.optString("family") ?: "N/A"
                )
                NutritionRow(
                    label = "calories",
                    value = fruitData?.getJSONObject("nutritions")?.getDouble("calories")?.toString()?.plus(" kcal") ?: "0 kcal"
                )
                NutritionRow(
                    label = "fat",
                    value = fruitData?.getJSONObject("nutritions")?.getDouble("fat")?.toString()?.plus(" g") ?: "0 g"
                )
                NutritionRow(
                    label = "sugar",
                    value = fruitData?.getJSONObject("nutritions")?.getDouble("sugar")?.toString()?.plus(" g") ?: "0 g"
                )
                NutritionRow(
                    label = "carbohydrates",
                    value = fruitData?.getJSONObject("nutritions")?.getDouble("carbohydrates")?.toString()?.plus(" g") ?: "0 g"
                )
                NutritionRow(
                    label = "protein",
                    value = fruitData?.getJSONObject("nutritions")?.getDouble("protein")?.toString()?.plus(" g") ?: "0 g"
                )
            }

        }else {
            // Show random image if fruit score is optimal
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your fruit intake is already optimal!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = "https://picsum.photos/300/200?random=${System.currentTimeMillis()}",
                    contentDescription = "Random image",
                    modifier = Modifier
                        .width(350.dp)
                        .height(250.dp)
                )
            }
        }

        // Divider
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )

        // GenAI Section
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MOTIVATIONAL MESSAGE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ElevatedButton(
                    onClick = { genAiViewModel.generateMotivationalMessage(patient.value) }
                ) {
                    Text("Motivational Message (AI) ")
                    Icon(Icons.Default.Send, contentDescription = "Generate message")
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    when (uiState) {
                        is UiState.Initial -> {
                            Text(
                                text = "Click the button to get a motivational message about your fruit intake!",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is UiState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        is UiState.Success -> {
                            val message = (uiState as UiState.Success).outputText
                            Text(
                                text = message,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        is UiState.Error -> {
                            Text(
                                text = (uiState as UiState.Error).errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ElevatedButton(
                    onClick = { showAllTips = true }
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Tips")
                    Text("Show All Tips")
                }
            }
        }

        val tipsViewModel: TipsViewModel = viewModel(
            factory = TipsViewModelFactory(LocalContext.current.applicationContext as Application)
        )

        // Get the list of tips
        val tips by tipsViewModel.getTipsByUserId(userId).collectAsState(initial = emptyList())

        // save tips
        LaunchedEffect(uiState) {
            if (uiState is UiState.Success) {
                val message = (uiState as UiState.Success).outputText
                tipsViewModel.insertTip(NutriCoachTip(userId = userId, message = message))
            }
        }

        // show the tips in the card
        if (showAllTips) {
            AlertDialog(
                onDismissRequest = { showAllTips = false },
                title = { Text("Your Saved Tips") },
                text = {
                    Column {
                        if (tips.isEmpty()) {
                            Text("No tips saved yet")
                        } else {
                            tips.forEach { tip ->
                                Text(
                                    text = "• ${tip.message}",
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showAllTips = false }
                    ) {
                        Text("Done")
                    }
                }
            )
        }

    }
}


@Composable
fun NutritionRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 22.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Normal
        )
        Text(
            text = ": $value",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Normal
        )
    }
}

// Get the fruit API
private fun fetchFruitData(fruitName: String, callback: (JSONObject?, String?) -> Unit) {
    Thread {
        try {
            val url = URL("https://www.fruityvice.com/api/fruit/$fruitName")
            val jsonString = url.readText()
            val jsonObject = JSONObject(jsonString)
            callback(jsonObject, null)
        } catch (e: Exception) {
            callback(null, "Error fetching fruit data: ${e.message}")
        }
    }.start()
}

class TipsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TipsViewModel(application) as T
    }
}