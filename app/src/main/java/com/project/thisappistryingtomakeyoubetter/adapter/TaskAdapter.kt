package com.project.thisappistryingtomakeyoubetter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class TaskAdapter(
    private val context: Context,
    private val tasks: List<TaskWithLabel>,
    private val listener: TaskCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var date = 0
    private val TAG = "TaskAdapter"

    fun resetDate() {
        this.date = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_task, parent, false)
        return TaskViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val task = tasks[position].task
        val labels = tasks[position].labels
        val adapter = LabelTaskAdapter(context, labels)

        val holder = viewHolder as TaskViewHolder

        // Setter
        holder.title.text = task.title
        holder.title.isChecked = task.isFinish
        holder.label.adapter = adapter
        holder.label.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)

        // Check Description
        checkDescription(task, holder)

        // This, on multiple item
        holder.title.setOnClickListener {
            task.isFinish = holder.title.isChecked
            task.labels = labels.toMutableList()
            listener.onBoxChecked(tasks[position])
        }
        holder.wrapper.setOnLongClickListener {
            listener.onLongClick(tasks[position])
            true
        }
        holder.title.setOnLongClickListener {
            listener.onLongClick(tasks[position])
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

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: CheckBox = itemView.findViewById(R.id.title)
        var description: TextView = itemView.findViewById(R.id.description)
        var wrapper: CardView = itemView.findViewById(R.id.wrapper)
        var label: RecyclerView = itemView.findViewById(R.id.labels)
    }

    interface TaskCallback {
        fun onLongClick(task: TaskWithLabel)
        fun onBoxChecked(task: TaskWithLabel)
    }
}