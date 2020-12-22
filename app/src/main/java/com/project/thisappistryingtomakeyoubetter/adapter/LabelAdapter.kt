package com.project.thisappistryingtomakeyoubetter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.model.Label

class LabelAdapter (
        private val context: Context,
        private val labels: List<Label>,
        private val mode: Int
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var deleteListener: ((label: Label)->Unit)?=null
    var editListener:((label: Label)->Unit)?=null

    companion object{
        @JvmStatic
        val CARD = 1
        @JvmStatic
        val TEXT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(mode){
            TEXT -> {
                val v = LayoutInflater.from(context).inflate(R.layout.view_label_text, parent, false)
                return LabelTextViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(context).inflate(R.layout.view_label, parent, false)
                return LabelViewHolder(v)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val label = labels[position]
        if(mode == CARD) {
            holder as LabelViewHolder
            holder.labelName.text = label.name
            label.color?.let { holder.labelIcon.setColorFilter(it) }

            holder.cardLayout.setOnLongClickListener {
                holder.textView.visibility = View.GONE
                holder.textEdit.visibility = View.VISIBLE
                holder.editLabel.setText(label.name)
                deleteListener?.invoke(label)
                true
            }

            holder.textInputLayout.setEndIconOnClickListener {
                holder.textView.visibility = View.VISIBLE
                holder.textEdit.visibility = View.GONE
                holder.colorView.visibility = View.GONE
                label.name = holder.editLabel.text.toString()
                editListener?.invoke(label)
            }

            holder.textInputLayout.setStartIconOnClickListener {
                holder.colorView.visibility = View.VISIBLE
                generateAdapter(holder.colorView)
            }
        } else {
            holder as LabelTextViewHolder
            holder.labelName.text = label.name
            label.color?.let { holder.labelIcon.setColorFilter(it) }
        }
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    private fun generateAdapter(colorView: RecyclerView){
        val adapter = ColorAdapter(context, context.resources.getIntArray(R.array.color_array).toList())
        colorView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        colorView.adapter = adapter
    }

    inner class LabelViewHolder(view: View): RecyclerView.ViewHolder(view){
        val labelIcon: ImageView = view.findViewById(R.id.label_icon)
        val labelName: TextView  = view.findViewById(R.id.label_name)
        val cardLayout: CardView = view.findViewById(R.id.card_layout)
        val textView: ConstraintLayout = view.findViewById(R.id.text_view)
        val textEdit: ConstraintLayout = view.findViewById(R.id.text_edit)
        val textInputLayout: TextInputLayout = view.findViewById(R.id.textlayout)
        val editLabel: TextInputEditText = view.findViewById(R.id.edit_label)
        val colorView: RecyclerView = view.findViewById(R.id.color_view)
    }

    inner class LabelTextViewHolder(view: View): RecyclerView.ViewHolder(view){
        val labelIcon: ImageView = view.findViewById(R.id.label_icon)
        val labelName: TextView  = view.findViewById(R.id.label_name)
    }
}