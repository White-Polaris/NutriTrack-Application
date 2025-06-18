package com.junyan34075453.nutritrack.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_intakes",
    foreignKeys = [ForeignKey(
        entity = Patient::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FoodIntake(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String, // Foreign key to Patient
    val fruitsChecked: Boolean,
    val vegetablesChecked: Boolean,
    val grainsChecked: Boolean,
    val redMeatChecked: Boolean,
    val seafoodChecked: Boolean,
    val poultryChecked: Boolean,
    val fishChecked: Boolean,
    val eggsChecked: Boolean,
    val nutsSeedsChecked: Boolean,
    val selectedPersona: String,
    val mealTime: String,
    val sleepTime: String,
    val wakeTime: String
)