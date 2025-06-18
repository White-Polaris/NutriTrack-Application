package com.junyan34075453.nutritrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Patient::class, FoodIntake::class, NutriCoachTip::class],
    version = 1,
    exportSchema = false
)
abstract class NutriTrackDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun foodIntakeDao(): FoodIntakeDao
    abstract fun nutriCoachTipDao(): NutriCoachTipDao

    companion object {
        @Volatile
        private var INSTANCE: NutriTrackDatabase? = null

        fun getDatabase(context: Context): NutriTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, NutriTrackDatabase::class.java, "nutritrack_db")
                    .build().also { INSTANCE = it }
            }
        }
    }
}