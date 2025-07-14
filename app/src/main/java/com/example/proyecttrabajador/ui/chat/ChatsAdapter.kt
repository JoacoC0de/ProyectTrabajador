package com.example.proyecttrabajador.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecttrabajador.data.model.Chat
import com.example.proyecttrabajador.databinding.ItemChatBinding

class ChatsAdapter(
    private val onChatClick: (Chat) -> Unit
) : ListAdapter<Chat, ChatsAdapter.ChatViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position), onChatClick)
    }

    class ChatViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat, onChatClick: (Chat) -> Unit) {
            val nombre = chat.client.name
            val apellido = chat.client.profile?.last_name ?: ""
            binding.txtUserName.text = "$nombre $apellido"
            binding.txtLastMessage.text = chat.lastMessage?.content ?: ""
            binding.root.setOnClickListener { onChatClick(chat) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat) = oldItem == newItem
    }
}