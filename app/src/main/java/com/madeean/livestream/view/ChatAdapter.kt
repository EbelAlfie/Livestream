package com.madeean.livestream.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.madeean.livestream.databinding.ItemChatsBinding

class ChatAdapter: RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private var chatList = mutableListOf<String>()

    class ChatViewHolder(val binding: ItemChatsBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(ItemChatsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.binding.tvComment.text = chatList[position]
    }

    fun addChat(text: String) {
        chatList.add(text)
        notifyItemInserted(chatList.size)
    }
}