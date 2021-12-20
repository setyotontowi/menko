package com.project.thisappistryingtomakeyoubetter

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import java.util.jar.Attributes

class ColorList : RecyclerView {

    var ctx: Context
    var adapter: Adapter

    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr) {
        this.ctx = ctx
        adapter = Adapter(ctx)
        setAdapter(adapter)
    }

    constructor(ctx: Context) : super(ctx) {
        this.ctx = ctx
        adapter = Adapter(ctx)
        setAdapter(adapter)
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int): super(ctx, attrs, defStyleAttr){
        this.ctx = ctx
        adapter = Adapter(ctx)
        setAdapter(adapter)
    }

    var list: List<Int> = listOf()
        set(list: List<Int>){
            field = list
            adapter.list = list
        }

    class Adapter(val context: Context): RecyclerView.Adapter<Adapter.ViewHolders>(){
        var list = listOf<Int>()
            set(value) {
                field = value
                //notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.view_palette, parent, false)
            return ViewHolders(v)
        }

        override fun onBindViewHolder(holder: ViewHolders, position: Int) {
            val color = list[position]
            holder.colorViews.setColorFilter(color)

            holder.colorViews.setOnClickListener{

            }
        }

        override fun getItemCount(): Int = list.size

        inner class ViewHolders(val view: View): RecyclerView.ViewHolder(view){
            var colorViews: ImageView = view.findViewById(R.id.color)
        }

    }



}