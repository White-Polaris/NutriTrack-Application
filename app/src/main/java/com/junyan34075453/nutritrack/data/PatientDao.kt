package com.junyan34075453.nutritrack.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPatient(patient: Patient)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updatePatient(patient: Patient)

    @Delete
    suspend fun deletePatient(patient: Patient)

    @Query("SELECT * FROM patients WHERE userId = :userId")
    suspend fun getPatientById(userId: String): Patient?

    @Query("SELECT * FROM patients WHERE userId = :userId AND phoneNumber = :phoneNumber")
    suspend fun getPatientByIdAndPhone(userId: String, phoneNumber: String): Patient?

    @Query("SELECT * FROM patients WHERE userId = :userId AND password = :password")
    suspend fun getPatientByIdAndPassword(userId: String, password: String): Patient?

    @Query("SELECT AVG(heifaTotalScoreMale) FROM patients WHERE sex = 'Male'")
    suspend fun getAverageMaleScore(): Double

    @Query("SELECT AVG(heifaTotalScoreFemale) FROM patients WHERE sex = 'Female'")
    suspend fun getAverageFemaleScore(): Double

    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<Patient>>

    @Query("SELECT userId FROM patients")
    fun getAllUserIds(): Flow<List<String>>
}