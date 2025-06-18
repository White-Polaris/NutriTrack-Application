package com.junyan34075453.nutritrack.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FoodIntakeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFoodIntake(foodIntake: FoodIntake)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateFoodIntake(foodIntake: FoodIntake)

    @Delete
    suspend fun deleteFoodIntake(foodIntake: FoodIntake)

    @Query("SELECT * FROM food_intakes WHERE userId = :userId")
    suspend fun getFoodIntakeByUserId(userId: String): FoodIntake?

    @Query("DELETE FROM food_intakes WHERE userId = :userId")
    suspend fun deleteFoodIntakeByUserId(userId: String)
}