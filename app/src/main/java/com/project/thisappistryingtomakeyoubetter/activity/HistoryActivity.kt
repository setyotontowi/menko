package com.project.thisappistryingtomakeyoubetter.activity

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.ActivityHistoryBinding
import com.project.thisappistryingtomakeyoubetter.databinding.DialogTaskBinding
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.util.TaskViewModel
import com.project.thisappistryingtomakeyoubetter.util.TaskViewModelProvider

class HistoryActivity : AppCompatActivity(), TaskAdapter.TaskCallback {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var toolbar: Toolbar
    private var adapter: TaskAdapter? = null
    private var tasks: MutableList<Task> = ArrayList()
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        toolbar = binding.toolbar
        setContentView(binding.root)
        setSupportActionBar(toolbar)

        val factory = TaskViewModelProvider(TaskViewModel(application, null, null))
        taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        adapter = TaskAdapter(this, tasks, this)
        binding.listTask.layoutManager = LinearLayoutManager(this)
        binding.listTask.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.tasks.observe(this, { tasks ->
            this.tasks.clear()
            this.tasks.addAll(tasks)
            adapter!!.notifyDataSetChanged()
        })
    }

    override fun onLongClick(task: Task?) {
        taskDialog(task)
    }

    override fun onBoxChecked(task: Task?) {
        taskViewModel.update(task)
    }

    private fun taskDialog(task: Task?) {
        val dialog = Dialog(this)
        val binding: DialogTaskBinding = DialogTaskBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.delete.visibility = View.VISIBLE

        // Match dialog window to screen width
        val window = dialog.window!!
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT)

        // Listeners
        binding.save.setOnClickListener{
            task?.let {
                task.title = binding.title.text.toString()
                task.description = binding.description.text.toString()
                taskViewModel.update(task)
            }
            dialog.dismiss()
        }

        binding.delete.setOnClickListener {
            task?.let { taskViewModel.delete(it) }
            dialog.dismiss()
        }

        dialog.show()
    }

}