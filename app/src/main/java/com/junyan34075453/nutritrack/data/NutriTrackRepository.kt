package com.junyan34075453.nutritrack.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import java.io.BufferedReader
import java.io.InputStreamReader

class NutriTrackRepository(context: Context) {
    // Property to hold the DAO instances
    private val patientDao: PatientDao
    private val foodIntakeDao: FoodIntakeDao
    private val nutriCoachTipDao: NutriCoachTipDao

    // Constructor to initialize the DAOs
    init {
        val database = NutriTrackDatabase.getDatabase(context)
        patientDao = database.patientDao()
        foodIntakeDao = database.foodIntakeDao()
        nutriCoachTipDao = database.nutriCoachTipDao()
    }

    private fun parseCsv(context: Context): List<Patient> {
        val patients = mutableListOf<Patient>()
        try {
            val inputStream = context.assets.open("account.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Skip header
            reader.readLine()

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val values = line!!.split(",")
                if (values.size > 1) {
                    val patient = Patient(
                        userId = values[1].trim(),
                        phoneNumber = values[0].trim(),
                        sex = values[2].trim(),
                        heifaTotalScoreMale = values[3].toDouble(),
                        heifaTotalScoreFemale = values[4].toDouble(),
                        discretionaryHeifaScoreMale = values[5].toDouble(),
                        discretionaryHeifaScoreFemale = values[6].toDouble(),
                        discretionaryServeSize = values[7].toDouble(),
                        vegetablesHeifaScoreMale = values[8].toDouble(),
                        vegetablesHeifaScoreFemale = values[9].toDouble(),
                        vegetablesWithLegumesAllocatedServeSize = values[10].toDouble(),
                        legumesAllocatedVegetables = values[11].toDouble(),
                        vegetablesVariationsScore = values[12].toDouble(),
                        vegetablesCruciferous = values[13].toDouble(),
                        vegetablesTuberAndBulb = values[14].toDouble(),
                        vegetablesOther = values[15].toDouble(),
                        legumes = values[16].toDouble(),
                        vegetablesGreen = values[17].toDouble(),
                        vegetablesRedAndOrange = values[18].toDouble(),
                        fruitHeifaScoreMale = values[19].toDouble(),
                        fruitHeifaScoreFemale = values[20].toDouble(),
                        fruitServeSize = values[21].toDouble(),
                        fruitVariationsScore = values[22].toDouble(),
                        fruitPome = values[23].toDouble(),
                        fruitTropicalAndSubtropical = values[24].toDouble(),
                        fruitBerry = values[25].toDouble(),
                        fruitStone = values[26].toDouble(),
                        fruitCitrus = values[27].toDouble(),
                        fruitOther = values[28].toDouble(),
                        grainsAndCerealsHeifaScoreMale = values[29].toDouble(),
                        grainsAndCerealsHeifaScoreFemale = values[30].toDouble(),
                        grainsAndCerealsServeSize = values[31].toDouble(),
                        grainsAndCerealsNonWholeGrains = values[32].toDouble(),
                        wholeGrainsHeifaScoreMale = values[33].toDouble(),
                        wholeGrainsHeifaScoreFemale = values[34].toDouble(),
                        wholeGrainsServeSize = values[35].toDouble(),
                        meatAndAlternativesHeifaScoreMale = values[36].toDouble(),
                        meatAndAlternativesHeifaScoreFemale = values[37].toDouble(),
                        meatAndAlternativesWithLegumesAllocatedServeSize = values[38].toDouble(),
                        legumesAllocatedMeatAndAlternatives = values[39].toDouble(),
                        dairyAndAlternativesHeifaScoreMale = values[40].toDouble(),
                        dairyAndAlternativesHeifaScoreFemale = values[41].toDouble(),
                        dairyAndAlternativesServeSize = values[42].toDouble(),
                        sodiumHeifaScoreMale = values[43].toDouble(),
                        sodiumHeifaScoreFemale = values[44].toDouble(),
                        sodiumMgMilligrams = values[45].toDouble(),
                        alcoholHeifaScoreMale = values[46].toDouble(),
                        alcoholHeifaScoreFemale = values[47].toDouble(),
                        alcoholStandardDrinks = values[48].toDouble(),
                        waterHeifaScoreMale = values[49].toDouble(),
                        waterHeifaScoreFemale = values[50].toDouble(),
                        water = values[51].toDouble(),
                        waterTotalMl = values[52].toDouble(),
                        beverageTotalMl = values[53].toDouble(),
                        sugarHeifaScoreMale = values[54].toDouble(),
                        sugarHeifaScoreFemale = values[55].toDouble(),
                        sugar = values[56].toDouble(),
                        saturatedFatHeifaScoreMale = values[57].toDouble(),
                        saturatedFatHeifaScoreFemale = values[58].toDouble(),
                        saturatedFat = values[59].toDouble(),
                        unsaturatedFatHeifaScoreMale = values[60].toDouble(),
                        unsaturatedFatHeifaScoreFemale = values[61].toDouble(),
                        unsaturatedFatServeSize = values[62].toDouble()
                    )
                    patients.add(patient)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return patients
    }

    // Patient operations
    suspend fun getPatientById(userId: String): Patient? = patientDao.getPatientById(userId)

    suspend fun getPatientByIdAndPhone(userId: String, phoneNumber: String): Patient? =
        patientDao.getPatientByIdAndPhone(userId, phoneNumber)

    suspend fun getPatientByIdAndPassword(userId: String, password: String): Patient? =
        patientDao.getPatientByIdAndPassword(userId, password)

    suspend fun insertPatient(patient: Patient) = patientDao.insertPatient(patient)

    suspend fun updatePatient(patient: Patient) = patientDao.updatePatient(patient)

    suspend fun deletePatient(patient: Patient) = patientDao.deletePatient(patient)

    suspend fun getAverageMaleScore(): Double = patientDao.getAverageMaleScore()

    suspend fun getAverageFemaleScore(): Double = patientDao.getAverageFemaleScore()

    fun getAllPatients(): Flow<List<Patient>> = patientDao.getAllPatients()

    fun getAllUserIds(): Flow<List<String>> = patientDao.getAllUserIds()

    // FoodIntake operations
    suspend fun getFoodIntakeByUserId(userId: String): FoodIntake? =
        foodIntakeDao.getFoodIntakeByUserId(userId)

    suspend fun insertFoodIntake(foodIntake: FoodIntake) =
        foodIntakeDao.insertFoodIntake(foodIntake)

    suspend fun updateFoodIntake(foodIntake: FoodIntake) =
        foodIntakeDao.updateFoodIntake(foodIntake)

    suspend fun deleteFoodIntake(foodIntake: FoodIntake) =
        foodIntakeDao.deleteFoodIntake(foodIntake)

    suspend fun deleteFoodIntakeByUserId(userId: String) =
        foodIntakeDao.deleteFoodIntakeByUserId(userId)

    // Database initialization
    suspend fun initializeDatabaseFromCsv(context: Context) {
        if (patientDao.getAllPatients() is Flow<List<Patient>>) { // Check if database is empty
            val patients = parseCsv(context)
            patients.forEach { patientDao.insertPatient(it) }
        }
    }
}