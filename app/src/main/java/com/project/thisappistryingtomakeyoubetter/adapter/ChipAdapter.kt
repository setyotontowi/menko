package com.project.thisappistryingtomakeyoubetter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.model.Label
import java.lang.NullPointerException

class ChipAdapter(
        private val context: Context,
        private val labels: MutableList<Label>
): RecyclerView.Adapter<ChipAdapter.ChipViewHolder>() {
    private val selectedLabels: MutableList<Label> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_chip, parent, false)
        return ChipViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val label = labels[position]
        holder.chip.text = label.name
        holder.chip.setOnCheckedChangeListener { _: CompoundButton, b: Boolean ->
            if(b){
                selectedLabels.add(label)
            } else {
                try {
                    selectedLabels.remove(label)
                }catch (e: NullPointerException){
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    fun getSelectedLabels(): MutableList<Label>{
        return this.selectedLabels
    }

    inner class ChipViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val chip: Chip = view.findViewById(R.id.chip)
    }
}