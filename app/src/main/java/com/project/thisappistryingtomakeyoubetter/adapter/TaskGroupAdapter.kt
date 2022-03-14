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
        listToMap(list).forEach {
            val date = it.key

            val diffCallback = TaskDiffCallback(this.list[date]?: listOf(), list)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            diffResult.dispatchUpdatesTo(this)

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

    class TaskDiffCallback(
            private val oldList: List<TaskWithLabel>,
            private val newList: List<TaskWithLabel>): DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].task.id == newList[newItemPosition].task.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].task.date == newList[newItemPosition].task.date
        }
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val date: TextView = view.findViewById(R.id.date)
        val viewList: RecyclerView = view.findViewById(R.id.list_task)
    }
}