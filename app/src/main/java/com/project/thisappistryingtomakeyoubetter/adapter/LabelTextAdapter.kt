package com.project.thisappistryingtomakeyoubetter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.model.Label

class LabelTextAdapter(
        private val context: Context,
        private val labels: List<Label>
): RecyclerView.Adapter<LabelTextAdapter.LabelTextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelTextViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_label_text, parent, false)
        return LabelTextViewHolder(v)
    }

    override fun onBindViewHolder(holder: LabelTextViewHolder, position: Int) {
        val label = labels[position]
        holder.labelName.text = label.name
        label.color?.let { holder.labelIcon.setColorFilter(it) }
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    inner class LabelTextViewHolder(view: View): RecyclerView.ViewHolder(view){
        val labelIcon: ImageView = view.findViewById(R.id.label_icon)
        val labelName: TextView = view.findViewById(R.id.label_name)
    }
}