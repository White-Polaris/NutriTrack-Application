package com.junyan34075453.nutritrack.screen

import android.annotation.SuppressLint
import android.app.Application
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.junyan34075453.nutritrack.R
import com.junyan34075453.nutritrack.data.FoodIntake
import com.junyan34075453.nutritrack.data.NutriTrackViewModel
import com.junyan34075453.nutritrack.screen.ui.theme.Assignment3Theme
import kotlinx.coroutines.launch
import java.util.Calendar

class Questionnaire : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userId = intent.getStringExtra("userId") ?: "default"
        setContent {
            Assignment3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Questionnaire(modifier = Modifier.padding(innerPadding), userId = userId)
                }
            }
        }
    }
}

// Data class to hold Persona information
data class PersonaInfo(
    val title: String,
    val description: String,
    val imageRes: Int     // image
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Questionnaire(modifier: Modifier = Modifier, userId: String = "default"){

    // Get the ViewModel
    val viewModel: NutriTrackViewModel = viewModel(
        factory = NutriTrackViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )

    // Kind of food
    var fruitsChecked by remember { mutableStateOf(false) }
    var vegetablesChecked by remember { mutableStateOf(false) }
    var grainsChecked by remember { mutableStateOf(false) }
    var redMeatChecked by remember { mutableStateOf(false) }
    var seafoodChecked by remember { mutableStateOf(false) }
    var poultryChecked by remember { mutableStateOf(false) }
    var fishChecked by remember { mutableStateOf(false) }
    var eggsChecked by remember { mutableStateOf(false) }
    var nutsSeedsChecked by remember { mutableStateOf(false) }

    // Persona choose menu
    var expanded by remember { mutableStateOf(false) }
    var selectedPersona by remember { mutableStateOf("") } // Select user persona
    val personas = listOf(
        "Health Devotee", "Mindful Eater", "Wellness Striver",
        "Balance Seeker", "Health Procrastinator", "Food Carefree"
    )

    // Persona dialog state
    var showPersonaDialog by remember { mutableStateOf(false) }
    var currentPersona by remember { mutableStateOf<PersonaInfo?>(null) }

    // Define Persona information
    val personasInfo = mapOf(
        "Health Devotee" to PersonaInfo(
            title = "Health Devotee",
            description = "I'm passionate about healthy eating and health plays a big part in my life. " +
                    " I use social media to follow active lifestyle personalities or get new recipes/exercises ideas. " +
                    " I may even buy superfoods or follow a particular type of diet. Like to think I am super healthy.",
            imageRes = R.drawable.persona_1 // image
        ),
        "Mindful Eater" to PersonaInfo(
            title = "Mindful Eater",
            description = "I’m health-conscious and being healthy and eating healthy is important to me." +
                    " Although health means different things to different people, I make conscious lifestyle decisions about eating based on what I believe healthy means." +
                    " I look for new recipes and healthy eating information on social media.",
            imageRes = R.drawable.persona_2
        ),
        "Wellness Striver" to PersonaInfo(
            title = "Wellness Striver",
            description = "I aspire to be healthy (but struggle sometimes)." +
                    " Healthy eating is hard work! I’ve tried to improve my diet, but always find things that make it difficult to stick with the changes." +
                    " Sometimes I notice recipe ideas or healthy eating hacks, and if it seems easy enough, I’ll give it a go.",
            imageRes = R.drawable.persona_3
        ),
        "Balance Seeker" to PersonaInfo(
            title = "Balance Seeker",
            description = "I try and live a balanced lifestyle, and I think that all foods are okay in moderation." +
                    " I shouldn’t have to feel guilty about eating a piece of cake now and again." +
                    " I get all sorts of inspiration from social media like finding out about new restaurants, fun recipes and sometimes healthy eating tips.",
            imageRes = R.drawable.persona_4
        ),
        "Health Procrastinator" to PersonaInfo(
            title = "Health Procrastinator",
            description = "I’m contemplating healthy eating but it’s not a priority for me right now." +
                    " I know the basics about what it means to be healthy, but it doesn’t seem relevant to me right now." +
                    " I have taken a few steps to be healthier but I am not motivated to make it a high priority because I have too many other things going on in my life." ,
            imageRes = R.drawable.persona_5
        ),
        "Food Carefree" to PersonaInfo(
            title = "Food Carefree",
            description = "I’m not bothered about healthy eating." +
                    " I don’t really see the point and I don’t think about it." +
                    " I don’t really notice healthy eating tips or recipes and I don’t care what I eat.",
            imageRes = R.drawable.persona_6
        )
    )

    // Timings state using TimePickerState
    var mealTime by remember { mutableStateOf("00:00") }
    var sleepTime by remember { mutableStateOf("00:00") }
    var wakeTime by remember { mutableStateOf("00:00") }

    // Get context for navigation and SharedPreferences
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("UserPrefs_$userId", Context.MODE_PRIVATE)
    }

    // Load saved state when the Composable is first launched
    LaunchedEffect(Unit) {
        // First check SharedPreferences
        fruitsChecked = sharedPreferences.getBoolean("${userId}_fruitsChecked", false)
        vegetablesChecked = sharedPreferences.getBoolean("${userId}_vegetablesChecked", false)
        grainsChecked = sharedPreferences.getBoolean("${userId}_grainsChecked", false)
        redMeatChecked = sharedPreferences.getBoolean("${userId}_redMeatChecked", false)
        seafoodChecked = sharedPreferences.getBoolean("${userId}_seafoodChecked", false)
        poultryChecked = sharedPreferences.getBoolean("${userId}_poultryChecked", false)
        fishChecked = sharedPreferences.getBoolean("${userId}_fishChecked", false)
        eggsChecked = sharedPreferences.getBoolean("${userId}_eggsChecked", false)
        nutsSeedsChecked = sharedPreferences.getBoolean("${userId}_nutsSeedsChecked", false)
        selectedPersona = sharedPreferences.getString("${userId}_selectedPersona", "") ?: ""
        mealTime = sharedPreferences.getString("${userId}_mealTime", "00:00") ?: "00:00"
        sleepTime = sharedPreferences.getString("${userId}_sleepTime", "00:00") ?: "00:00"
        wakeTime = sharedPreferences.getString("${userId}_wakeTime", "00:00") ?: "00:00"

        // Then check Room database (this will override SharedPreferences if data exists)
        val existingIntake = viewModel.getFoodIntakeByUserId(userId)
        if (existingIntake != null) {
            fruitsChecked = existingIntake.fruitsChecked
            vegetablesChecked = existingIntake.vegetablesChecked
            grainsChecked = existingIntake.grainsChecked
            redMeatChecked = existingIntake.redMeatChecked
            seafoodChecked = existingIntake.seafoodChecked
            poultryChecked = existingIntake.poultryChecked
            fishChecked = existingIntake.fishChecked
            eggsChecked = existingIntake.eggsChecked
            nutsSeedsChecked = existingIntake.nutsSeedsChecked
            selectedPersona = existingIntake.selectedPersona
            mealTime = existingIntake.mealTime
            sleepTime = existingIntake.sleepTime
            wakeTime = existingIntake.wakeTime
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Food Intake Questionnaire",
                        fontFamily = FontFamily.Cursive,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { context.startActivity(Intent(context, Home::class.java).apply { putExtra("userId", userId) }) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        // Screen
        Surface(
            modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            color = MaterialTheme.colorScheme.background
        ){

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Space
                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {}

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = "Tick all the food categories you can eat",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // First row boxTick
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Start
                ){
                    Box(modifier = Modifier.weight(0.9f)) {
                        CheckboxWithLabel("Fruits", fruitsChecked) { fruitsChecked = it }
                    }
                    Box(modifier = Modifier.weight(0.9f)) {
                        CheckboxWithLabel("Vegetables", vegetablesChecked) { vegetablesChecked = it }
                    }
                    Box(modifier = Modifier.weight(0.9f)) {
                        CheckboxWithLabel("Grains", grainsChecked) { grainsChecked = it }
                    }
                }

                // Second row boxTick
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ){
                    Box(modifier = Modifier.weight(0.9f)) {
                        CheckboxWithLabel("Red Meat", redMeatChecked) { redMeatChecked = it }
                    }
                    Box(modifier = Modifier.weight(0.9f)) {
                        CheckboxWithLabel("Seafood", seafoodChecked) { seafoodChecked = it }
                    }
                    Box(modifier = Modifier.weight(0.9f)) {
                        CheckboxWithLabel("Poultry", poultryChecked) { poultryChecked = it }
                    }
                }

                // Third row boxTick
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Box(modifier = Modifier.weight(0.9f)) {
                        CheckboxWithLabel("Fish", fishChecked) { fishChecked = it }
                    }
                    Box(modifier = Modifier.weight(0.9f)) {
                        CheckboxWithLabel("Eggs", eggsChecked) { eggsChecked = it }
                    }
                    Box(modifier = Modifier.weight(0.9f)) {
                        CheckboxWithLabel("Nuts/Seeds", nutsSeedsChecked) { nutsSeedsChecked = it }
                    }
                }

                // Persona bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = "Your Persona",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }

                // Persona under text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = "People can be broadly classified into 6 different types based on their eating preferences. Click on each button below to find out the different types, and select the type that best fits you!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Left,
                        fontFamily = FontFamily.Serif
                    )
                }

                // First row persona buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ElevatedButton(
                        onClick = {
                            currentPersona = personasInfo["Health Devotee"]
                            showPersonaDialog = true
                        },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(0.3f),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Health Devotee",
                            fontSize = 12.sp,
                        )
                    }
                    ElevatedButton(
                        onClick = {
                            currentPersona = personasInfo["Mindful Eater"]
                            showPersonaDialog = true
                        },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(0.4f),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Mindful Eater",
                            fontSize = 12.sp,
                        )
                    }
                    ElevatedButton(
                        onClick = {
                            currentPersona = personasInfo["Wellness Striver"]
                            showPersonaDialog = true
                        },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(0.7f),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Wellness Striver",
                            fontSize = 12.sp,
                        )
                    }
                }

                // Second row persona buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ElevatedButton(
                        onClick = {
                            currentPersona = personasInfo["Balance Seeker"]
                            showPersonaDialog = true
                        },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(0.3f),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Balance Seeker",
                            fontSize = 12.sp,
                        )
                    }
                    ElevatedButton(
                        onClick = {
                            currentPersona = personasInfo["Health Procrastinator"]
                            showPersonaDialog = true
                        },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(0.5f),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Health Procrastinator",
                            fontSize = 12.sp,
                        )
                    }
                    ElevatedButton(
                        onClick = {
                            currentPersona = personasInfo["Food Carefree"]
                            showPersonaDialog = true
                        },
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(0.7f),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Food Carefree",
                            fontSize = 12.sp,
                        )
                    }
                }

                // Persona Dialog
                if (showPersonaDialog && currentPersona != null) {
                    AlertDialog(
                        onDismissRequest = { showPersonaDialog = false },
                        title = { Text(text = currentPersona!!.title, fontWeight = FontWeight.Bold) },
                        text = {
                            Column {
                                Image(
                                    painter = painterResource(id = currentPersona!!.imageRes),
                                    contentDescription = currentPersona!!.title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = currentPersona!!.description)
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    selectedPersona = currentPersona!!.title
                                    showPersonaDialog = false
                                }
                            ) {
                                Text("Select This Persona")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showPersonaDialog = false }
                            ) {
                                Text("Dismiss")
                            }
                        }
                    )
                }

                // Space
                Spacer(modifier = Modifier.height(8.dp))

                // Choose persona
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = "Which persona best fits you?",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {expanded = !expanded}
                ) {
                    OutlinedTextField(
                        // username input field
                        value = selectedPersona,
                        onValueChange = { },
                        label = { Text(text = "Select your persona") },
                        readOnly = true,
                        placeholder = { Text(
                            text = "Choose a persona",
                            fontSize = 15.sp)},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .menuAnchor(),
                        shape = RoundedCornerShape(60.dp),
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        personas.forEach { persona ->
                            DropdownMenuItem(
                                text = { Text(text = persona) },
                                onClick = {
                                    selectedPersona = persona
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Space
                Spacer(modifier = Modifier.height(8.dp))

                // Timings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = "Timings",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }

                // Time input for meal time
                TimePickerField(
                    label = "What time of day approx. do you normally eat your biggest meal?",
                    time = mealTime,
                    onTimeChange = { mealTime = it }
                )

                // Time picker for sleep time
                TimePickerField(
                    label = "What time of day approx. do you go to sleep at night?",
                    time = sleepTime,
                    onTimeChange = { sleepTime = it }
                )

                // Time picker for wake time
                TimePickerField(
                    label = "What time of day approx. do you wake up in the morning?",
                    time = wakeTime,
                    onTimeChange = { wakeTime = it }
                )

                // Space
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ){
                    // Button save
                    ElevatedButton(
                        onClick = {
                            // Save state to SharedPreferences
                            with(sharedPreferences.edit()) {
                                putBoolean("${userId}_fruitsChecked", fruitsChecked)
                                putBoolean("${userId}_vegetablesChecked", vegetablesChecked)
                                putBoolean("${userId}_grainsChecked", grainsChecked)
                                putBoolean("${userId}_redMeatChecked", redMeatChecked)
                                putBoolean("${userId}_seafoodChecked", seafoodChecked)
                                putBoolean("${userId}_poultryChecked", poultryChecked)
                                putBoolean("${userId}_fishChecked", fishChecked)
                                putBoolean("${userId}_eggsChecked", eggsChecked)
                                putBoolean("${userId}_nutsSeedsChecked", nutsSeedsChecked)
                                putString("${userId}_selectedPersona", selectedPersona)
                                putString("${userId}_mealTime", mealTime)
                                putString("${userId}_sleepTime", sleepTime)
                                putString("${userId}_wakeTime", wakeTime)
                                apply()
                            }

                            // Create FoodIntake object
                            val foodIntake = FoodIntake(
                                userId = userId,
                                fruitsChecked = fruitsChecked,
                                vegetablesChecked = vegetablesChecked,
                                grainsChecked = grainsChecked,
                                redMeatChecked = redMeatChecked,
                                seafoodChecked = seafoodChecked,
                                poultryChecked = poultryChecked,
                                fishChecked = fishChecked,
                                eggsChecked = eggsChecked,
                                nutsSeedsChecked = nutsSeedsChecked,
                                selectedPersona = selectedPersona,
                                mealTime = mealTime,
                                sleepTime = sleepTime,
                                wakeTime = wakeTime
                            )

                            // Save to Room database
                            viewModel.viewModelScope.launch {
                                // Check if there's existing data for this user
                                val existingIntake = viewModel.getFoodIntakeByUserId(userId)

                                if (existingIntake != null) {
                                    // Update existing record
                                    viewModel.updateFoodIntake(foodIntake.copy(id = existingIntake.id))
                                } else {
                                    // Insert new record
                                    viewModel.insertFoodIntake(foodIntake)
                                }

                                Toast.makeText(context, "Data saved for User ID: $userId", Toast.LENGTH_SHORT).show()
                                context.startActivity(Intent(context, Home::class.java).apply { putExtra("userId", userId) })
                                (context as? ComponentActivity)?.finish()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.4f),
        //                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6600)),
                        shape = RoundedCornerShape(15.dp),

                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Text("Save")
                    }
                }
            }
        }
    }
}

// Reusable TimePickerField composable
@Composable
fun TimePickerField(
    label: String,
    time: String,
    onTimeChange: (String) -> Unit
) {
    val context = LocalContext.current

    // Function to show TimePickerDialog
    val showTimePicker = {
        val mCalendar = Calendar.getInstance()
        val mHour = mCalendar.get(Calendar.HOUR_OF_DAY)
        val mMinute = mCalendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, mHour: Int, mMinute: Int ->
                val formattedTime = String.format("%02d:%02d", mHour, mMinute)
                onTimeChange(formattedTime)
            },
            mHour,
            mMinute,
            true // 24-hour format
        ).show()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = FontFamily.Serif,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        OutlinedTextField(
            value = time,
            onValueChange = { },
            readOnly = true,
            modifier = Modifier
                .width(120.dp)
                .height(54.dp),
            shape = RoundedCornerShape(5.dp),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_recent_history),  // clock icon
                    contentDescription = "Select time",
                    modifier = Modifier.clickable { showTimePicker() }
                )
            }
        )
    }
}

// Method of Checkbox
@Composable
fun CheckboxWithLabel(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF38D700),
                uncheckedColor = Color(0xFFA6A6A6)
            )
        )
        Text(
            text = label,
            fontSize = 12.sp
        )
    }
}
