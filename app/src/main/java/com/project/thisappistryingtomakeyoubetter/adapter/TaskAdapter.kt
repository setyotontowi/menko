package com.project.thisappistryingtomakeyoubetter.adapter

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter.TaskViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.project.thisappistryingtomakeyoubetter.R
import android.widget.CheckBox
import android.widget.TextView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper

class TaskAdapter(
        private val context: Context,
        private val tasks: List<Task>,
        private val listener: TaskCallback,
        private val mode: Int
) : RecyclerView.Adapter<TaskViewHolder>() {
    private var date = 0
    private val TAG = "TaskAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_task, parent, false)
        return TaskViewHolder(v)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.title.isChecked = task.isFinish

        // Check Description
        checkDescription(task, holder)

        // Check Date
        if(!checkDate(task) && mode == GeneralHelper.MODE_HISTORY){
            holder.date.visibility = View.VISIBLE
            holder.date.text = GeneralHelper.dateFormatter().format(task.date)
        } else {
            holder.date.visibility = View.GONE
        }

        // This, on multiple item
        holder.title.setOnClickListener {
            task.isFinish = holder.title.isChecked
            listener.onBoxChecked(task)
        }
        holder.wrapper.setOnLongClickListener {
            listener.onLongClick(task)
            true
        }
        holder.title.setOnLongClickListener {
            listener.onLongClick(task)
            true
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    private fun checkDescription(task: Task, holder: TaskViewHolder) {
        if (task.description == "" || task.description == null) {
            holder.description.visibility = View.GONE
        } else {
            holder.description.visibility = View.VISIBLE
            holder.description.text = task.description
        }
    }

    private fun checkDate(task: Task): Boolean{
        val mDate = task.date.date
        Log.d(TAG, "checkDate: $mDate")
        if(mDate != this.date){
            this.date = mDate
            return false
        }
        return true
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.date)
        var title: CheckBox = itemView.findViewById(R.id.title)
        var description: TextView = itemView.findViewById(R.id.description)
        var wrapper: CardView = itemView.findViewById(R.id.wrapper)
    }

    interface TaskCallback {
        fun onLongClick(task: Task?)
        fun onBoxChecked(task: Task?)
    }
}