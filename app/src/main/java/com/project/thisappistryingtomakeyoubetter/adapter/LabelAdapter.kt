package com.project.thisappistryingtomakeyoubetter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.model.Label

class LabelAdapter(
        private val context: Context,
        private val labels: List<Label>
): RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {
    private val TAG = "LabelAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_label, parent, false)
        return LabelViewHolder(v)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        val label = labels[position]
        holder.labelName.text = label.name
        Log.d(TAG, "onBindViewHolder: color ${label.color}")
        label.color?.let { holder.labelIcon.setColorFilter(it) }
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    inner class LabelViewHolder(view: View): RecyclerView.ViewHolder(view){
        val labelIcon: ImageView = view.findViewById(R.id.label_icon)
        val labelName: TextView  = view.findViewById(R.id.label_name)
    }
}