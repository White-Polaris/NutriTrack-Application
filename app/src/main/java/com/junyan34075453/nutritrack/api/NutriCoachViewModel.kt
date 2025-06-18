package com.junyan34075453.nutritrack.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.junyan34075453.nutritrack.BuildConfig
import com.junyan34075453.nutritrack.data.Patient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NutriCoachViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    fun generateMotivationalMessage(patient: Patient?) {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prompt = buildPrompt(patient)
                val response = generativeModel.generateContent(prompt)

                response.text?.let { message ->
                    _uiState.value = UiState.Success(message)
                } ?: run {
                    _uiState.value = UiState.Error("No message generated")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private fun buildPrompt(patient: Patient?): String {
        if (patient == null) {
            return "Generate a short encouraging message (under 35 words, with emoji) about maintaining a balanced diet."
        }

        val genderPrefix = if (patient.sex == "Male") "He" else "She"
        val scores = """
        Vegetables: ${if (patient.sex == "Male") patient.vegetablesHeifaScoreMale else patient.vegetablesHeifaScoreFemale}/10
        Fruit: ${if (patient.sex == "Male") patient.fruitHeifaScoreMale else patient.fruitHeifaScoreFemale}/10
        Grains & Cereal: ${if (patient.sex == "Male") patient.grainsAndCerealsHeifaScoreMale else patient.grainsAndCerealsHeifaScoreFemale}/10
        Whole Grains: ${if (patient.sex == "Male") patient.wholeGrainsHeifaScoreMale else patient.wholeGrainsHeifaScoreFemale}/10
        Meat & Alternatives: ${if (patient.sex == "Male") patient.meatAndAlternativesHeifaScoreMale else patient.meatAndAlternativesHeifaScoreFemale}/10
        Dairy: ${if (patient.sex == "Male") patient.dairyAndAlternativesHeifaScoreMale else patient.dairyAndAlternativesHeifaScoreFemale}/10
        Water: ${if (patient.sex == "Male") patient.waterHeifaScoreMale else patient.waterHeifaScoreFemale}/5
        Unsaturated Fats: ${if (patient.sex == "Male") patient.saturatedFatHeifaScoreMale else patient.saturatedFatHeifaScoreFemale}/5
        Sodium: ${if (patient.sex == "Male") patient.sodiumHeifaScoreMale else patient.sodiumHeifaScoreFemale}/5
        Sugar: ${if (patient.sex == "Male") patient.sugarHeifaScoreMale else patient.sugarHeifaScoreFemale}/10
        Alcohol: ${if (patient.sex == "Male") patient.alcoholHeifaScoreMale else patient.alcoholHeifaScoreFemale}/5
        Discretionary Food: ${if (patient.sex == "Male") patient.discretionaryHeifaScoreMale else patient.discretionaryHeifaScoreFemale}/10
    """.trimIndent()

        return """
        Generate a personalized, encouraging health message (under 35 words, with emoji) based on these nutrition scores:
        $scores
        
        Message should:
        1. Highlight their strongest area (highest score)
        2. Suggest improvement in weakest area (lowest score)
        3. Keep it positive and actionable
        4. Include one specific food suggestion
        5. Use simple language and 1-2 emojis
        
    """.trimIndent()
    }
}