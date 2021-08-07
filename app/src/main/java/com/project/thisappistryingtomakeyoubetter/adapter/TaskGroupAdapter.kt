package com.project.thisappistryingtomakeyoubetter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.model.TaskGroup
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper

class TaskGroupAdapter(
    val callback: TaskAdapter.TaskCallback
): RecyclerView.Adapter<TaskGroupAdapter.ViewHolder>() {

    var list = mutableListOf<TaskGroup>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_task_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.date.text = GeneralHelper.dateFormatter().format(item.date)
        holder.viewList.apply {
            adapter = TaskAdapter(context, item.tasks, callback)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun getItemCount(): Int = list.size

    fun addList(list: List<TaskGroup>){
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val date: TextView = view.findViewById(R.id.date)
        val viewList: RecyclerView = view.findViewById(R.id.list_task)
    }
}