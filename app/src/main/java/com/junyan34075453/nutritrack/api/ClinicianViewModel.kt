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

class ClinicianViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ClinicianUiState>(ClinicianUiState.Initial)
    val uiState: StateFlow<ClinicianUiState> = _uiState.asStateFlow()

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    fun analyzePatientData(patients: List<Patient>) {
        _uiState.value = ClinicianUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dataSummary = buildDataSummary(patients)
                val patterns = generateDataPatterns(dataSummary)
                _uiState.value = ClinicianUiState.Success(patterns)
            } catch (e: Exception) {
                _uiState.value = ClinicianUiState.Error(e.localizedMessage ?: "Error analyzing data")
            }
        }
    }

    private fun buildDataSummary(patients: List<Patient>): String {
        if (patients.isEmpty()) return "No patient data available"

        val malePatients = patients.filter { it.sex == "Male" }
        val femalePatients = patients.filter { it.sex == "Female" }

        return """
            Total patients: ${patients.size}
            Male patients: ${malePatients.size}
            Female patients: ${femalePatients.size}
            
            Average scores:
            - Male HEIFA: ${malePatients.map { it.heifaTotalScoreMale }.average()}
            - Female HEIFA: ${femalePatients.map { it.heifaTotalScoreFemale }.average()}
            
            Male Vegetables: ${malePatients.map { it.vegetablesHeifaScoreMale }.average()}
            Female Vegetables: ${femalePatients.map { it.vegetablesHeifaScoreFemale }.average()}
            Male Grains&Cereals: ${malePatients.map { it.grainsAndCerealsHeifaScoreMale }.average()}
            Female Grains&Cereals: ${femalePatients.map { it.grainsAndCerealsHeifaScoreFemale }.average()}
            Male Fruits: ${malePatients.map { it.fruitHeifaScoreMale }.average()}
            Female Fruits: ${femalePatients.map { it.fruitHeifaScoreFemale }.average()}
            Male Water: ${malePatients.map { it.waterHeifaScoreMale }.average()}
            Female Water: ${femalePatients.map { it.waterHeifaScoreFemale }.average()}
            Male Meat&Alternatives: ${malePatients.map { it.meatAndAlternativesHeifaScoreMale }.average()}
            Female Meat&Alternatives: ${femalePatients.map { it.meatAndAlternativesHeifaScoreFemale }.average()}
            Male Sugar: ${malePatients.map { it.sugarHeifaScoreMale }.average()}
            Female Sugar: ${femalePatients.map { it.sugarHeifaScoreFemale }.average()}
            Male Alcohol: ${malePatients.map { it.alcoholHeifaScoreMale }.average()}
            Female Alcohol: ${femalePatients.map { it.alcoholHeifaScoreFemale }.average()}
        """.trimIndent()
    }

    private suspend fun generateDataPatterns(dataSummary: String): List<String> {
        return try {
            val prompt = """
            Analyze this nutrition data and identify EXACTLY 3 significant patterns:
            $dataSummary
            
            Requirements:
            1. Provide exactly 3 bullet points
            2. Each point must be a complete sentence(only 50 words with some emoji inside the sentence)
            3. Include specific interesting things without number detail
            4. Format as:
                1. First pattern...
                2. Second pattern...
                3. Third pattern...
        """.trimIndent()

            val response = generativeModel.generateContent(prompt)
            response.text?.split("\n")
                ?.filter { it.trim().matches(Regex("""^\d\..+""")) }
                ?.take(3)
                ?.map { it.substringAfter(".").trim() }
                ?: listOf("No significant patterns found", "", "")
        } catch (e: Exception) {
            listOf(
                "Error: ${e.message ?: "Analysis failed"}",
                "Please check your API configuration",
                "Contact support if problem persists"
            )
        }
    }
}