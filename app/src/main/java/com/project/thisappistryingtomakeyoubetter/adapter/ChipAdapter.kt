package com.project.thisappistryingtomakeyoubetter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.model.Label

class ChipAdapter(
        private val context: Context,
        private val labels: MutableList<Label>
): RecyclerView.Adapter<ChipAdapter.ChipViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_chip, parent, false)
        return ChipViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val label = labels[position]
        holder.chip.text = label.name
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    inner class ChipViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val chip: Chip = view.findViewById(R.id.chip)
    }
}