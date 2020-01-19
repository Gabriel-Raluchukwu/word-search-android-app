package com.example.wordsearch.gameFinishedFragment

import androidx.lifecycle.ViewModel
import timber.log.Timber

class GameFinishedViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        Timber.d("GameFinishedViewModel cleared")
    }
}
