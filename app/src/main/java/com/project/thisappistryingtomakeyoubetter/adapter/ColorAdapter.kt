package com.project.thisappistryingtomakeyoubetter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.project.thisappistryingtomakeyoubetter.R

class ColorAdapter(
        private val context: Context,
        private val colors: List<Int>
): RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    var listener: ((color:Int) -> Unit)?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_palette, parent, false)
        return ColorViewHolder(v)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors[position]
        holder.colorView.setColorFilter(color)

        holder.colorView.setOnClickListener{
            listener?.invoke(color)
        }
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    inner class ColorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var colorView = itemView.findViewById<ImageView>(R.id.color_view)
    }
}