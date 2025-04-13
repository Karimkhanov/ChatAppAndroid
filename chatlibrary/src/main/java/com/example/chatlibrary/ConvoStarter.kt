package com.example.chatlibrary

import android.content.Context
import android.content.Intent

// Объект-утилита для запуска активности чата
object ConvoStarter {
    // Метод запуска активности чата
    fun launch(context: Context) {
        val intent = Intent(context, ConvoActivity::class.java)
        context.startActivity(intent)
    }
}
