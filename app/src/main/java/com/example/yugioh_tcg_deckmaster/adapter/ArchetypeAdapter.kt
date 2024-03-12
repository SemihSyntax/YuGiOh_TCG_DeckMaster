package com.example.yugioh_tcg_deckmaster.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.yugioh_tcg_deckmaster.data.datamodels.Archetype
import com.example.yugioh_tcg_deckmaster.databinding.ItemArchetypeBinding
import com.example.yugioh_tcg_deckmaster.ui.ArchetypeFragmentDirections

class ArchetypeAdapter(

) : RecyclerView.Adapter<ArchetypeAdapter.ItemViewHolder>(){


    private var dataset = listOf<Archetype>()
    inner class ItemViewHolder(val binding: ItemArchetypeBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Archetype>) {
        dataset = newList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemArchetypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val archetype = dataset[position]

        holder.binding.tvArchetype.text = archetype.archetype_name

        holder.binding.mcvArchetype.setOnClickListener {
            holder.itemView.findNavController().navigate(ArchetypeFragmentDirections.actionArchetypeFragmentToArchetypeDetailFragment(archetype.archetype_name))
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}