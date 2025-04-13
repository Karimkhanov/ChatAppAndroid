package com.example.chatlibrary

// Модель сообщения для чата
data class ChatMsg(
    val text: String,       // Текст сообщения
    val isSent: Boolean     // true, если сообщение отправлено, false – получено
)
