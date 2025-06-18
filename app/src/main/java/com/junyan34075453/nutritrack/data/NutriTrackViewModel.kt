package com.junyan34075453.nutritrack.data

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NutriTrackViewModel(application: Application) : AndroidViewModel(application) {
    // Repository instance
    private val repository: NutriTrackRepository

    init {
        repository = NutriTrackRepository(application.applicationContext)
    }

    // Patient operations
    suspend fun getPatientById(userId: String) = repository.getPatientById(userId)

    suspend fun getPatientByIdAndPhone(userId: String, phoneNumber: String) =
        repository.getPatientByIdAndPhone(userId, phoneNumber)

    suspend fun getPatientByIdAndPassword(userId: String, password: String): Patient? {
        val patient = getPatientById(userId)
        return if (patient?.password == password) patient else null
    }


    fun insertPatient(patient: Patient) = viewModelScope.launch {
        repository.insertPatient(patient)
    }

    fun updatePatient(patient: Patient) = viewModelScope.launch {
        repository.updatePatient(patient)
    }

    fun deletePatient(patient: Patient) = viewModelScope.launch {
        repository.deletePatient(patient)
    }

    fun getAllPatients(): Flow<List<Patient>> = repository.getAllPatients()

    // get all user id
    fun getAllUserIds(): Flow<List<String>> = repository.getAllUserIds()

    // get average
    suspend fun getAverageMaleScore(): Double = repository.getAverageMaleScore()

    suspend fun getAverageFemaleScore(): Double = repository.getAverageFemaleScore()

    // FoodIntake operations
    suspend fun getFoodIntakeByUserId(userId: String) = repository.getFoodIntakeByUserId(userId)

    fun insertFoodIntake(foodIntake: FoodIntake) = viewModelScope.launch {
        repository.insertFoodIntake(foodIntake)
    }

    fun updateFoodIntake(foodIntake: FoodIntake) = viewModelScope.launch {
        repository.updateFoodIntake(foodIntake)
    }

    fun deleteFoodIntake(foodIntake: FoodIntake) = viewModelScope.launch {
        repository.deleteFoodIntake(foodIntake)
    }

    fun deleteFoodIntakeByUserId(userId: String) = viewModelScope.launch {
        repository.deleteFoodIntakeByUserId(userId)
    }

    // Database initialization
    fun initializeDatabase(context: Context) = viewModelScope.launch {
        repository.initializeDatabaseFromCsv(context)
    }

    // ViewModel Factory
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NutriTrackViewModel::class.java)) {
                return NutriTrackViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    // keeping login
    fun setLoggedInUser(context: Context, userId: String) {
        context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE).edit {
            putString("loggedInUserId", userId)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }

    fun getLoggedInUser(context: Context): String? {
        return context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
            .getString("loggedInUserId", null)
    }

    fun clearLoggedInUser(context: Context) {
        context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE).edit {
            remove("loggedInUserId")
            putBoolean("isLoggedIn", false)
            apply()
        }
    }
}