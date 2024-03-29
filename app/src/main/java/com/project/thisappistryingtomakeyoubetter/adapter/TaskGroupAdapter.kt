package com.project.thisappistryingtomakeyoubetter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.model.TaskGroup
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

class TaskGroupAdapter(
        var list: MutableMap<Date, MutableList<TaskWithLabel>>,
        val callback: TaskAdapter.TaskCallback
): RecyclerView.Adapter<TaskGroupAdapter.ViewHolder>() {

    val dateSet = mutableMapOf<Int, Date>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_task_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = dateSet[position]
        val item = list[date]
        date?.let {
            holder.date.text = GeneralHelper.dateFormatter().format(date)
            holder.viewList.apply {
                adapter = TaskAdapter(context, item?: mutableListOf(), callback)
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun addListTaskWithLabel(list: List<TaskWithLabel>){
        this.list.clear()
        this.dateSet.clear()
        listToMap(list).forEach {
            val date = it.key

            val existedList = this.list[date]
            if(existedList.isNullOrEmpty()) {
                val newList = (this.list[date]?: mutableListOf())
                newList.addAll(it.value)
                this.list[date] = newList
                this.dateSet[this.dateSet.size] = date
            } else {
                existedList.addAll(it.value)
                this.list[date] = existedList
                this.dateSet[this.dateSet.size-1] = date
            }
        }

        notifyDataSetChanged()
    }

    // Create map from task list by parsing it from data from the model
    // list(task1, task2, task3)
    // into [map(1-10-2023, [task 1, task 2]), map(2-10-2023, [task 3])]
    private fun listToMap(list: List<TaskWithLabel>): MutableMap<Date, List<TaskWithLabel>>{
        val map = mutableMapOf<Date, List<TaskWithLabel>>()
        list.forEach {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date =
                    formatter.parse(formatter.format(it.task.date ?: Date())) ?: Date()
            val a = map[date]
            if (a == null) {
                map[date] = listOf(it)
            } else {
                val b = a.toMutableList()
                b.add(it)
                map[date] = b
            }
        }
        return map
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val date: TextView = view.findViewById(R.id.date)
        val viewList: RecyclerView = view.findViewById(R.id.list_task)
    }
}