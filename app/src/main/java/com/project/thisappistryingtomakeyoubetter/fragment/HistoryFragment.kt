package com.project.thisappistryingtomakeyoubetter.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.adapter.ChipAdapter
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter
import com.project.thisappistryingtomakeyoubetter.adapter.TaskGroupAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.DialogTaskBinding
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentHistoryBinding
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskGroup
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import com.project.thisappistryingtomakeyoubetter.viewmodel.MainViewModel
import com.project.thisappistryingtomakeyoubetter.viewmodel.TaskViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class HistoryFragment : Fragment(), TaskAdapter.TaskCallback, GeneralHelper.ConfirmDialog {

    private lateinit var binding: FragmentHistoryBinding
    private val labels: MutableList<Label> = ArrayList()

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val taskViewModel: TaskViewModel by viewModels { vmFactory }
    private val mainViewModel: MainViewModel by activityViewModels { vmFactory }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as App).appComponent.inject(this)
        setHasOptionsMenu(true)
        mainViewModel.stateFromOutsideMainFragment.value = true

        requireActivity().title = "History"

        setupView()

        taskViewModel.setFrom(null)
        taskViewModel.setTo(null)
        taskViewModel.setPage(-1)
        taskViewModel.apply {
            label.observe(viewLifecycleOwner) { handleLabel(it) }
            taskGroup.observe(viewLifecycleOwner) { handleTaskGroup(it) }
        }
    }

    var taskAdapter = TaskGroupAdapter(this@HistoryFragment)
    private fun setupView() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.listTask.apply {
            adapter = taskAdapter
            layoutManager = linearLayoutManager
        }

        var previousTotal = 0
        var firstVisibleItem: Int
        var visibleItemCount: Int
        var totalItemCount: Int
        val visibleTreshold = 5
        var loading = true


        binding.listTask.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                /*visibleItemCount = recyclerView.childCount
                totalItemCount = linearLayoutManager.itemCount
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleTreshold)) {
                    val page = taskViewModel.page.value ?: 0
                    taskViewModel.setPage(page + 1)
                    loading = true
                }*/

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_option, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_all -> {
                deleteAll()
                return true
            }
        }
        return false
    }

    override fun onLongClick(task: TaskWithLabel) {
        taskDialog(task)
    }

    override fun onBoxChecked(task: TaskWithLabel) {
        taskViewModel.update(task.task)
    }

    private fun handleTaskGroup(it: List<TaskGroup>?) {
        Log.d("DEBUGGING", "handleTaskGroup: ${it?.size}")
        it?.let {
            placeHolder(true)
            taskAdapter.list.clear()
            taskAdapter.addList(it)
        } ?: run {
            placeHolder(false)
        }
    }

    private fun handleLabel(labels: List<Label>?) {
        this.labels.clear()
        this.labels.addAll(labels ?: listOf())
    }

    private fun deleteAll() {
        GeneralHelper.confirmationDialog(
                context,
                getString(R.string.history_delete_all_confirm),
                this
        )
    }

    /** Confirmation Dialog implementation */
    override fun onPositive(dialogInterface: DialogInterface) {
        taskViewModel.deleteAll()
        dialogInterface.dismiss()
    }

    override fun onNegative(dialogInterface: DialogInterface) {
        dialogInterface.dismiss()
    }

    private fun placeHolder(isExist: Boolean) {
        if (!isExist) {
            binding.listTask.visibility = View.GONE
            binding.nodata.visibility = View.VISIBLE
        } else {
            binding.listTask.visibility = View.VISIBLE
            binding.nodata.visibility = View.GONE
        }
    }

    private fun taskDialog(task: TaskWithLabel?) {
        BottomSheetDialog(requireContext()).apply {
            val binding = DialogTaskBinding.inflate(
                layoutInflater
            )
            setContentView(binding.root)


            // Chip Adapter
            val chipAdapter = ChipAdapter(requireContext(), labels)


            // Views Setup
            if (task != null) {
                chipAdapter.setSelectedLabels(task.labels)
                binding.title.setText(task.task.title)
                binding.description.setText(task.task.description)
                binding.delete.visibility = View.VISIBLE
            }
            binding.labels.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.labels.adapter = chipAdapter


            // Listeners
            binding.save.setOnClickListener {
                task?.let { task ->
                    task.task.title = Objects.requireNonNull(binding.title.text).toString()
                    task.task.description = Objects.requireNonNull(binding.description.text).toString()
                    task.task.labels = chipAdapter.getSelectedLabels()
                    taskViewModel.update(task.task)
                }
                dismiss()
            }
            binding.delete.setOnClickListener {
                if (task != null) {
                    taskViewModel.delete(task.task)
                }
                dismiss()
            }

            /*setOnShowListener {
                val behavior = BottomSheetBehavior.from(binding.layout)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }*/

            show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}