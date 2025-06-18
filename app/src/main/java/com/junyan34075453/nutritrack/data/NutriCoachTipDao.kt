package com.junyan34075453.nutritrack.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NutriCoachTipDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTip(tip: NutriCoachTip)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTip(tip: NutriCoachTip)

    @Delete
    suspend fun deleteTip(tip: NutriCoachTip)

    @Query("SELECT * FROM nutri_coach_tips WHERE userId = :userId ORDER BY id DESC")
    fun getTipsByUserId(userId: String): Flow<List<NutriCoachTip>>
}