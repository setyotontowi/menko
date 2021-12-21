package com.project.thisappistryingtomakeyoubetter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ColorList : RecyclerView {

    var ctx: Context
    var adapter: Adapter
    var listener: ((color:Int) -> Unit)? = null

    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr) {
        this.ctx = ctx
        adapter = Adapter(ctx) {
            listener?.invoke(it)
        }
        setAdapter(adapter)
    }

    constructor(ctx: Context) : super(ctx) {
        this.ctx = ctx
        adapter = Adapter(ctx) {
            listener?.invoke(it)
        }
        setAdapter(adapter)
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyleAttr: Int): super(ctx, attrs, defStyleAttr){
        this.ctx = ctx
        adapter = Adapter(ctx) {
            listener?.invoke(it)
        }
        setAdapter(adapter)
    }

    var list: List<Int> = listOf()
        set(list: List<Int>){
            field = list
            adapter.list = list
        }

    class Adapter(val context: Context, val listener:((color:Int) -> Unit)): RecyclerView.Adapter<Adapter.ViewHolders>(){
        var list = listOf<Int>()
            set(value) {
                field = value
                //notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
            val v = LayoutInflater.from(context).inflate(R.layout.view_palette, parent, false)
            return ViewHolders(v)
        }

        override fun onBindViewHolder(holder: ViewHolders, position: Int) {
            val color = list[position]
            holder.colorViews.setColorFilter(color)

            holder.colorViews.setOnClickListener{
                listener.invoke(color)
            }
        }

        override fun getItemCount(): Int = list.size

        inner class ViewHolders(view: View): RecyclerView.ViewHolder(view){
            var colorViews: ImageView = view.findViewById(R.id.color)
        }

    }



}