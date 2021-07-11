package com.project.thisappistryingtomakeyoubetter.fragment

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.project.thisappistryingtomakeyoubetter.viewmodel.TaskViewModel
import java.util.*
import javax.inject.Inject

class DayFragment : Fragment(), View.OnClickListener, TaskCallback {
    private var calendar: Calendar? = null
    private lateinit var binding: FragmentDayBinding
    private val tasks: MutableList<TaskWithLabel> = ArrayList()
    private val labels: MutableList<Label> = ArrayList()
    private var taskAdapter: TaskAdapter? = null
    private var position = 0
    private lateinit var taskViewModel: TaskViewModel

    @JvmField
    @Inject
    var vmFactory: ViewModelProvider.Factory? = null
    var from: Date? = null
    var to: Date? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assert(arguments != null)
        calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH)
        calendar?.timeInMillis = requireArguments().getLong(DATE)
        position = requireArguments().getInt(POSITION)
        (requireActivity().application as App).appComponent.inject(this)
        setHasOptionsMenu(true)
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

        from = GeneralHelper.fromDate(calendar)
        to = GeneralHelper.toDate(calendar)

        // Showing Task List
        taskAdapter = TaskAdapter(requireActivity(), tasks, this)
        binding.task.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        // Floating Action Button Add
        binding.addTask.setOnClickListener(this)

        // Set task ViewModel
        taskViewModel = ViewModelProvider(this, vmFactory!!).get(TaskViewModel::class.java)
        taskViewModel.setFrom(from)
        taskViewModel.setTo(to)
        taskViewModel.apply {
            tasksWithLabel.observe(viewLifecycleOwner) { handleListTask(it) }
            label.observe(viewLifecycleOwner) { handleLabel(it) }
        }

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

    override fun onClick(v: View) {
        if (v.id == R.id.add_task) {
            taskDialog(null)
        }
    }

    override fun onLongClick(task: TaskWithLabel) {
        taskDialog(task)
    }

    override fun onBoxChecked(task: TaskWithLabel) {
        taskViewModel.update(task.task)
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
        val dialog = Dialog(requireContext())
        val binding = DialogTaskBinding.inflate(
            layoutInflater
        )
        dialog.setContentView(binding.root)

        // Hide Keyboard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialog.window)
                ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }

        // Chip Adapter
        val chipAdapter = ChipAdapter(requireContext(), labels)

        // Match dialog window to screen width
        val window = dialog.window!!
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

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
            dialog.dismiss()
        }
        binding.delete.setOnClickListener {
            if (task != null) {
                taskViewModel.delete(task.task)
            }
            dialog.dismiss()
        }
        dialog.show()
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