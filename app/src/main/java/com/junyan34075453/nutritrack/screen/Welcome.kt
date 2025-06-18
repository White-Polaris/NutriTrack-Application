package com.junyan34075453.nutritrack.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.junyan34075453.nutritrack.R
import com.junyan34075453.nutritrack.data.NutriTrackViewModel
import com.junyan34075453.nutritrack.screen.ui.theme.Assignment3Theme

class Welcome : ComponentActivity() {
    private lateinit var viewModel: NutriTrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewModel using Factory
        viewModel = ViewModelProvider(this, NutriTrackViewModel.Factory(application))
            .get(NutriTrackViewModel::class.java)

        // check if login already
        val loggedInUserId = viewModel.getLoggedInUser(this)
        if (loggedInUserId != null) {
            // jump into the Home page
            startActivity(Intent(this, Home::class.java).apply {
                putExtra("userId", loggedInUserId)
            })
            finish()
            return
        }

        // Initialize database on first launch
        viewModel.initializeDatabase(this)

        setContent {
            Assignment3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun WelcomeScreen(modifier: Modifier = Modifier){

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){

//        Box {
//            // Background
//            Image(
//                painter = painterResource(id = R.drawable.welcome_background),
//                contentDescription = "Background",
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
//        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Title and Logo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // First of the headline
                Text(
                    text = "NutriTrack",
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive
                )
                // Add image at the top
                Image(
                    painter = painterResource(id = R.drawable.health_logo),
                    contentDescription = "NutriTrack Logo",
                    modifier = Modifier.size(255.dp) // Adjust size as needed
                )
                // add space between logo and username
                Spacer(modifier = Modifier.height(24.dp))
            }

            // statement
            Text(
                text = "This app provides general health and nutrition information for educational purposes only. " +
                        "It is not intended as medical advice diagnosis, or treatment. " +
                        "Always consult a qualified healthcare professional before making any changes to your diet, exercise, or health regimen. " +
                        "Use this app at your own risk. If you'd like to an Accredited Practicing Dietitian (APD). " +
                        "Please visit the Monash Nutrition/Dietetics Clinic (discounted rates for students): https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif
            )

            // Outside webpage
            val context = LocalContext.current
            TextButton(
                onClick = {
                    // Using GPT
                    val uri = "https://www.monash.edu/medicine/scs/nutrition/clinics/nutrition"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    context.startActivity(intent)
                }
            ) {
                // text view
                Text(text = "Monash Nutrition Clinic ↗",
                )
            }

            // Login Button
            ElevatedButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(text = "Login")
            }

            // student ID
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "Li Junyan (34075453)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }

            // Bottom Sheet
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    LoginScreen(
                        onLoginSuccess = { userId ->
                            showBottomSheet = false // Close bottom sheet
                            val intent = Intent(context, Questionnaire::class.java).apply {
                                putExtra("userId", userId)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}