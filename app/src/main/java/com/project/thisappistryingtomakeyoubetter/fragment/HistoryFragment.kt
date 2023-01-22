package com.project.thisappistryingtomakeyoubetter.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.isNotEmpty
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.adapter.ChipAdapter
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter
import com.project.thisappistryingtomakeyoubetter.adapter.TaskGroupAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentHistoryBinding
import com.project.thisappistryingtomakeyoubetter.databinding.LayoutFilterBinding
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import com.project.thisappistryingtomakeyoubetter.view.toggle
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import com.project.thisappistryingtomakeyoubetter.viewmodel.HistoryViewModel
import com.project.thisappistryingtomakeyoubetter.viewmodel.MainViewModel
import javax.inject.Inject
import kotlin.collections.ArrayList


class HistoryFragment : Fragment(), TaskAdapter.TaskCallback, GeneralHelper.ConfirmDialog {

    private lateinit var binding: FragmentHistoryBinding
    private val labels: MutableList<Label> = ArrayList()

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    // TODO: This cannot be allowed having two viewmodels in one activity
    @Inject
    lateinit var taskViewModel: HistoryViewModel
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
        if(mainViewModel.standAlone.value == false){
            mainViewModel.stateFromOutsideMainFragment.value = true
        }

        requireActivity().title = "History"

        setupView()
        taskViewModel.apply {
            label.observe(viewLifecycleOwner) { handleLabel(it) }
            summary.observe(viewLifecycleOwner) { handleSummary(it) }
            taskHistory.observe(viewLifecycleOwner) { handleTaskWithLabel(it) }
            taskFilter.observe(viewLifecycleOwner) { handleTaskWithLabel(it) }
        }

        try {
            val labelFiltered = arguments?.getSerializable(EXTRA_FILTER) as Label
            taskViewModel.filterLabel = listOf(labelFiltered)
            taskViewModel.filter()
        } catch (e: java.lang.Exception) {
            e.stackTrace
        }
    }

    private var taskAdapter = TaskGroupAdapter(mutableMapOf(), this@HistoryFragment)
    private fun setupView() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.listTask.apply {
            adapter = taskAdapter
            layoutManager = linearLayoutManager
        }

        val behavior = BottomSheetBehavior.from(binding.linearLayout)
        behavior.peekHeight = arguments?.getInt(EXTRA_PEEK_HEIGHT, 1700)?:1700

        if(mainViewModel.standAlone.value == false) {
            binding.linearLayout.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else if (scrollY == 0) {
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            })
        } else {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

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
            R.id.action_filter -> {
                showFilterDialog()
            }
        }
        return false
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if(!hidden){
            requireActivity().title = "History"
        }
        super.onHiddenChanged(hidden)
    }

    override fun onBoxChecked(task: TaskWithLabel) {
        taskViewModel.update(task.task)
    }

    override fun onUpdate(task: Task) {
        taskViewModel.update(task)
    }

    override fun onDelete(task: Task) {
        taskViewModel.delete(task)
    }

    override fun onLongClick(task: TaskWithLabel) {

    }

    private fun handleSummary(summary: Triple<Int, Int, Int>){
        val component = binding.componentSummary
        component.apply {
            tvAll.text = summary.first.toString()
            tvFinished.text = summary.second.toString()
            tvUnfinished.text = summary.third.toString()
        }
    }

    private fun handleTaskWithLabel(it: List<TaskWithLabel>?) {
        it?.let {
            placeHolder(true)
            taskAdapter.addListTaskWithLabel(it)
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

    private fun showFilterDialog(){
        BottomSheetDialog(requireContext()).apply {
            val binding = LayoutFilterBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.apply {
                layoutLabel.toggle(labels.isNotEmpty())
                val listLabel = this@HistoryFragment.labels
                val chipAdapter = ChipAdapter(requireContext(), listLabel) {
                    taskViewModel.filterLabel = it
                    taskViewModel.filter()
                }
                chipAdapter.setSelectedLabels(taskViewModel.filterLabel?: mutableListOf())

                labels.apply {
                    adapter = chipAdapter
                    layoutManager = FlexboxLayoutManager(requireContext())
                }

                taskViewModel.filterCompleted.let {
                    when (it) {
                        true -> chipCompleted.isChecked = true
                        false -> chipUncompleted.isChecked = true
                        null -> {
                            chipCompleted.isChecked = false
                            chipUncompleted.isChecked = false
                        }
                    }
                }

                chipStatus.setOnCheckedChangeListener { group, checkedId ->
                    val completed = chipCompleted.isChecked
                    val unCompleted = chipUncompleted.isChecked
                    taskViewModel.filterCompleted(completed, unCompleted)
                    taskViewModel.filter()
                }
            }
            show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
        const val STANDALONE = "standalone"
        const val EXTRA_FILTER = "filter"
        const val EXTRA_PEEK_HEIGHT = "peek"
    }
}