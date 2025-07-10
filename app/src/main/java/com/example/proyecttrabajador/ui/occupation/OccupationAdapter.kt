package com.example.proyecttrabajador.ui.occupation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecttrabajador.data.model.Occupation
import com.example.proyecttrabajador.databinding.ItemOccupationBinding

class OccupationAdapter(
    private val occupations: List<Occupation>,
    private val selected: Set<Int>,
    private val onChecked: (Int) -> Unit
) : RecyclerView.Adapter<OccupationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemOccupationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOccupationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = occupations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val occupation = occupations[position]
        holder.binding.checkBox.text = occupation.name
        holder.binding.checkBox.isChecked = selected.contains(occupation.id)
        holder.binding.checkBox.setOnClickListener {
            onChecked(occupation.id)
        }
    }
}