package com.junyan34075453.nutritrack.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class TipsRepository(private val nutriCoachTipDao: NutriCoachTipDao) {

    suspend fun insertTip(tip: NutriCoachTip) = nutriCoachTipDao.insertTip(tip)

    fun getTipsByUserId(userId: String): Flow<List<NutriCoachTip>> =
        nutriCoachTipDao.getTipsByUserId(userId)

    suspend fun deleteTip(tip: NutriCoachTip) = nutriCoachTipDao.deleteTip(tip)
}