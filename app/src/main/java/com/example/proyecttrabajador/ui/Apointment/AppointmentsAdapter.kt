package com.example.proyecttrabajador.ui.Apointment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecttrabajador.data.model.Appointment
import com.example.proyecttrabajador.databinding.ItemChatBinding

class AppointmentsAdapter : ListAdapter<Appointment, AppointmentsAdapter.AppointmentViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    class AppointmentViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(appointment: Appointment) {
            val nombre = appointment.client.name
            val apellido = appointment.client.profile?.last_name ?: ""
            binding.txtUserName.text = "$nombre $apellido"
            binding.txtLastMessage.text = appointment.chat?.lastMessage?.content ?: "Sin mensajes"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment) = oldItem == newItem
    }

    }
