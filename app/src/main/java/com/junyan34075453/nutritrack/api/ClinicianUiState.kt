package com.junyan34075453.nutritrack.api

sealed interface ClinicianUiState {
    object Initial : ClinicianUiState
    object Loading : ClinicianUiState
    data class Success(val patterns: List<String>) : ClinicianUiState
    data class Error(val errorMessage: String) : ClinicianUiState
}
