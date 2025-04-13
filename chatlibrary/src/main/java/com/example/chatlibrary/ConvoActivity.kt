package com.example.chatlibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatlibrary.databinding.ActivityChatBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

// Активность чата; устанавливает UI и соединение с WebSocket сервером
class ConvoActivity : AppCompatActivity() {

    // Связывание с layout через View Binding
    private lateinit var binding: ActivityChatBinding
    // Адаптер списка сообщений
    private lateinit var adapter: ConvoAdapter
    // Переменная для WebSocket-соединения
    private var webSocket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()  // Инициализируем RecyclerView для списка сообщений
        connectWebSocket()   // Устанавливаем соединение с сервером

        // Обработчик кнопки отправки сообщения
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString()
            if (message.isNotBlank()) {
                adapter.addMessage(ChatMsg(message, true))  // Добавляем сообщение как отправленное
                webSocket?.send(message)                      // Отправляем сообщение на сервер
                binding.messageInput.text.clear()             // Очищаем поле ввода после отправки
            }
        }
    }

    // Настройка RecyclerView с LinearLayoutManager
    private fun setupRecyclerView() {
        adapter = ConvoAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    // Функция подключения к WebSocket серверу
    private fun connectWebSocket() {
        val client = OkHttpClient()  // Создаем клиента OkHttp
        val request = Request.Builder()
            .url("wss://echo.websocket.org")
            .build()                 // Формируем запрос
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            // Обработка полученного текстового сообщения
            override fun onMessage(webSocket: WebSocket, text: String) {
                runOnUiThread {
                    // Если пришло специальное сообщение, заменить его на альтернативный текст
                    val displayText = if (text == "203 = 0xcb") "🎉 Special Code Received!" else text
                    adapter.addMessage(ChatMsg(displayText, false))
                }
            }
            // Обработка бинарных сообщений (например, изображений или других данных)
            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                runOnUiThread {
                    adapter.addMessage(ChatMsg("📦 Binary: ${bytes.hex()}", false))
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // Закрываем соединение, когда активность уничтожается
        webSocket?.close(1000, null)
    }
}
