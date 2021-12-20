package com.project.thisappistryingtomakeyoubetter

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ColorList(val ctx: Context) : RecyclerView(ctx) {

    var adapter = ColorList.Adapter()

    var list: List<Int> = listOf()
        set(list: List<Int>){
            field = list
            adapter.list = list
        }

    class Adapter: RecyclerView.Adapter<ViewHolder>(){
        var list = listOf<Int>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.view_palette, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val color = list[position]
            holder.colorView.setColorFilter(color)

            holder.colorView.setOnClickListener{

            }
        }

        override fun getItemCount(): Int = list.size

    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var colorView: ImageView = itemView.findViewById(R.id.color_view)
    }

    init {
        super.setAdapter(adapter)
    }
}