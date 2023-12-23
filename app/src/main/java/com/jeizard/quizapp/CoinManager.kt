package com.jeizard.quizapp

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableIntStateOf


private const val COINS_KEY = "coins"
private const val COIN_PREFERENCES_KEY = "coin_preferences"

class CoinManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        COIN_PREFERENCES_KEY,
        Context.MODE_PRIVATE
    )

    private val coinCount = mutableIntStateOf(sharedPreferences.getInt(COINS_KEY, 300))

    fun getCoins(): Int {
        return coinCount.value
    }

    fun addCoins(amount: Int) {
        coinCount.value += amount
    }

    fun removeCoins(amount: Int): Boolean {
        if (coinCount.value >= amount) {
            coinCount.value -= amount
            return true
        }
        return false
    }

    fun saveCoins() {
        sharedPreferences.edit().putInt(COINS_KEY, coinCount.value).commit()
    }
}