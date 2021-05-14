package com.project.thisappistryingtomakeyoubetter.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.DialogTaskBinding
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentHistoryBinding
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import com.project.thisappistryingtomakeyoubetter.util.TaskViewModel
import javax.inject.Inject


class HistoryFragment : Fragment(), TaskAdapter.TaskCallback, GeneralHelper.ConfirmDialog {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var toolbar: Toolbar
    private lateinit var taskAdapter: TaskAdapter
    private var tasks: MutableList<TaskWithLabel> = ArrayList()
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
        setHasOptionsMenu(true)

        taskAdapter = TaskAdapter(requireActivity(), tasks, this, GeneralHelper.MODE_HISTORY)
        binding.listTask.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        taskViewModel.setFrom(null)
        taskViewModel.setTo(null)
        taskViewModel.apply {
            tasksWithLabel.observe(viewLifecycleOwner){handleListTask(it)}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_option, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_all -> {
                deleteAll()
                return true
            }
        }
        return false
    }

    override fun onLongClick(task: TaskWithLabel) {
        taskDialog(task.task)
    }

    override fun onBoxChecked(task: TaskWithLabel) {
        taskViewModel.update(task.task)
    }

    private fun handleListTask(tasks: List<TaskWithLabel>?) {
        this.tasks.clear()
        if (tasks != null) {
            this.tasks.addAll(tasks)
        }
        taskAdapter.resetDate()
        taskAdapter.notifyDataSetChanged()
        placeHolder()
    }

    private fun deleteAll(){
        GeneralHelper.confirmationDialog(context,
                getString(R.string.history_delete_all_confirm),
                this)
    }

    /** Confirmation Dialog implementation */
    override fun onPositive(dialogInterface: DialogInterface) {
        taskViewModel.deleteAll()
        dialogInterface.dismiss()
    }

    override fun onNegative(dialogInterface: DialogInterface) {
        dialogInterface.dismiss()
    }

    private fun placeHolder() {
        if (tasks.isEmpty()) {
            binding.listTask.visibility = View.GONE
            binding.nodata.visibility = View.VISIBLE
        } else {
            binding.listTask.visibility = View.VISIBLE
            binding.nodata.visibility = View.GONE
        }
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
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}