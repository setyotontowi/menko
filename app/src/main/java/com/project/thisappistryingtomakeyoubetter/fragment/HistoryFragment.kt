package com.project.thisappistryingtomakeyoubetter.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.DialogTaskBinding
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentHistoryBinding
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import com.project.thisappistryingtomakeyoubetter.util.TaskViewModel
import javax.inject.Inject


class HistoryFragment : Fragment(), TaskAdapter.TaskCallback {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var toolbar: Toolbar
    private var adapter: TaskAdapter? = null
    private var tasks: MutableList<Task> = ArrayList()
    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val taskViewModel: TaskViewModel by viewModels { vmFactory}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.inject(this)

        adapter = TaskAdapter(requireContext(), tasks, this, GeneralHelper.MODE_HISTORY)
        binding.listTask.layoutManager = LinearLayoutManager(requireContext())
        binding.listTask.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.get(null, null).observe(this, { tasks ->
            this.tasks.clear()
            if (tasks != null) {
                this.tasks.addAll(tasks)
            }
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
        val dialog = Dialog(requireContext())
        val binding: DialogTaskBinding = DialogTaskBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.delete.visibility = View.VISIBLE

        // Views Setup
        if (task != null) {
            binding.title.setText(task.title)
            binding.description.setText(task.description)
            binding.delete.visibility = View.VISIBLE
        }

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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HistoryFragment.
         */
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}