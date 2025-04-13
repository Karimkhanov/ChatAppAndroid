package com.example.chatlibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatlibrary.databinding.ItemMessageSentBinding
import com.example.chatlibrary.databinding.ItemMessageReceivedBinding

// Адаптер для списка сообщений в чате
class ConvoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Список сообщений, хранящий все ChatMsg объекты
    private val chatMsgs = mutableListOf<ChatMsg>()

    // Функция добавления сообщения в список и обновление адаптера
    fun addMessage(chatMsg: ChatMsg) {
        chatMsgs.add(chatMsg)
        notifyItemInserted(chatMsgs.size - 1) // Оповещаем адаптер о добавлении нового элемента
    }

    // Определение типа представления в зависимости от того, отправлено сообщение или получено
    override fun getItemViewType(position: Int): Int {
        return if (chatMsgs[position].isSent) 1 else 0
    }

    // Создание ViewHolder в зависимости от типа сообщения
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val binding = ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentViewHolder(binding)
        } else {
            val binding = ItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedViewHolder(binding)
        }
    }

    // Привязка данных сообщения к ViewHolder-у
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatMsgs[position]
        if (holder is SentViewHolder) {
            holder.binding.textView.text = message.text
        } else if (holder is ReceivedViewHolder) {
            holder.binding.textView.text = message.text
        }
    }

    // Возвращает общее количество сообщений
    override fun getItemCount(): Int = chatMsgs.size

    // ViewHolder для сообщений, отправленных пользователем
    inner class SentViewHolder(val binding: ItemMessageSentBinding) : RecyclerView.ViewHolder(binding.root)
    // ViewHolder для сообщений, полученных от сервера
    inner class ReceivedViewHolder(val binding: ItemMessageReceivedBinding) : RecyclerView.ViewHolder(binding.root)
}
