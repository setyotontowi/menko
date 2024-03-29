package com.project.thisappistryingtomakeyoubetter.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.activity.MainActivity
import com.project.thisappistryingtomakeyoubetter.adapter.ChipAdapter
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter
import com.project.thisappistryingtomakeyoubetter.adapter.TaskAdapter.TaskCallback
import com.project.thisappistryingtomakeyoubetter.databinding.DialogTaskBinding
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentDayBinding
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.Task
import com.project.thisappistryingtomakeyoubetter.model.TaskWithLabel
import com.project.thisappistryingtomakeyoubetter.util.GeneralHelper
import com.project.thisappistryingtomakeyoubetter.viewmodel.DayViewModel
import java.util.*
import javax.inject.Inject

class DayFragment : Fragment(), View.OnClickListener, TaskCallback {
    private var calendar: Calendar? = null
    private lateinit var binding: FragmentDayBinding
    private val tasks: MutableList<TaskWithLabel> = ArrayList()
    private val labels: MutableList<Label> = ArrayList()
    private var taskAdapter: TaskAdapter? = null
    private var position = 0

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory

    private lateinit var taskViewModel: DayViewModel

    var from: Date? = null
    var to: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).appComponent.inject(this)
        setHasOptionsMenu(true)
        assert(arguments != null)

        calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH)
        calendar?.timeInMillis = requireArguments().getLong(DATE)
        position = requireArguments().getInt(POSITION)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setupView()
    }

    override fun onResume() {
        super.onResume()
        taskAdapter!!.notifyDataSetChanged()
        val title: String = when (position) {
            -1 -> getString(R.string.title_yesterday)
             0 -> getString(R.string.title_today)
             1 -> getString(R.string.title_tomorrow)
            else -> GeneralHelper.dateFormatter().format(calendar!!.time)
        }
        (requireParentFragment().requireActivity() as MainActivity).toolbar.title = title
    }

    private fun initData(){
        from = GeneralHelper.fromDate(calendar)
        to = GeneralHelper.toDate(calendar)

        // Set task ViewModel
        taskViewModel = ViewModelProvider(this, vmFactory).get(DayViewModel::class.java)
        taskViewModel.init(from, to, -1)

        taskViewModel.apply {
            tasksWithLabel.observe(viewLifecycleOwner) { handleListTask(it) }
            label.observe(viewLifecycleOwner) { handleLabel(it) }
        }
    }

    private fun setupView(){
        // Showing Task List
        taskAdapter = TaskAdapter(requireActivity(), tasks, this)
        binding.task.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // Floating Action Button Add
        binding.addTask.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.add_task) {
            taskDialog(null)
        }
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
        taskDialog(task)
    }

    private fun handleLabel(labels: List<Label>?) {
        this.labels.clear()
        this.labels.addAll(labels ?: listOf())
    }

    private fun handleListTask(list: List<TaskWithLabel>?) {
        tasks.clear()
        tasks.addAll(list ?: listOf())
        taskAdapter?.notifyDataSetChanged()
        placeHolder()
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
                if (task == null) {
                    val task1 = Task(
                        Objects.requireNonNull(binding.title.text).toString(),
                        Objects.requireNonNull(binding.description.text).toString(),
                        calendar!!.time
                    )
                    task1.labels = chipAdapter.getSelectedLabels()
                    taskViewModel.insert(task1)
                } else {
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

            show()
        }
    }

    private fun placeHolder() {
        if (tasks.isEmpty()) {
            binding.task.visibility = View.GONE
            binding.nodata.visibility = View.VISIBLE
        } else {
            binding.task.visibility = View.VISIBLE
            binding.nodata.visibility = View.GONE
        }
    }

    companion object {
        const val DATE = "date"
        const val POSITION = "position"

        @JvmStatic
        fun newInstance(calendar: Calendar, position: Int): DayFragment {
            val fragment = DayFragment()
            val args = Bundle()
            args.putLong(DATE, calendar.timeInMillis)
            args.putInt(POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }
}