package com.junyan34075453.nutritrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutri_coach_tips")
data class NutriCoachTip(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,  // connect user id
    val message: String
)