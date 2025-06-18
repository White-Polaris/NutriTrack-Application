package com.junyan34075453.nutritrack.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TipsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TipsRepository

    init {
        val tipsDao = NutriTrackDatabase.getDatabase(application).nutriCoachTipDao()
        repository = TipsRepository(tipsDao)
    }

    fun insertTip(tip: NutriCoachTip) = viewModelScope.launch {
        repository.insertTip(tip)
    }

    fun getTipsByUserId(userId: String) = repository.getTipsByUserId(userId)

    fun deleteTip(tip: NutriCoachTip) = viewModelScope.launch {
        repository.deleteTip(tip)
    }
}