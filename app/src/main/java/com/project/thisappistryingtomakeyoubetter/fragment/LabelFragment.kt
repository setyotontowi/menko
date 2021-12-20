package com.project.thisappistryingtomakeyoubetter.fragment

import android.app.Activity
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.thisappistryingtomakeyoubetter.App
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.adapter.ColorAdapter
import com.project.thisappistryingtomakeyoubetter.adapter.LabelAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentLabelBinding
import com.project.thisappistryingtomakeyoubetter.model.Label
import com.project.thisappistryingtomakeyoubetter.model.LabelWithTask
import com.project.thisappistryingtomakeyoubetter.viewmodel.LabelViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [LabelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LabelFragment : Fragment() {

    private val TAG = "LabelFragment"
    private var isOpen = false
    private var color: Int = 0
    private var labels: MutableList<LabelWithTask> = ArrayList()
    private lateinit var binding: FragmentLabelBinding
    private lateinit var label: Label
    private lateinit var adapter: LabelAdapter

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val labelViewModel: LabelViewModel by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentLabelBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.inject(this)
        requireActivity().title = "Label"

        // Color Adapter
        val colorAdapter = ColorAdapter(requireContext(), resources.getIntArray(R.array.color_array).toList())
        binding.colorView.list = resources.getIntArray(R.array.color_array).toList()

        this.color = binding.addLabel.currentTextColor

        // Label Adapter
        adapter = LabelAdapter(requireContext(), labels)
        binding.label.layoutManager = LinearLayoutManager(requireContext())
        binding.label.adapter = adapter

        binding.colorView.visibility = if(isOpen) View.VISIBLE else View.GONE

        // Click Listener
        binding.addTextlayout.setEndIconOnClickListener {
            if(binding.addLabel.text.toString() == ""){
                Toast.makeText(requireContext(),
                        getString(R.string.label_empty_notification),
                        Toast.LENGTH_LONG).show()
            } else {
                label = Label(0, binding.addLabel.text.toString(), this.color)
                //labels.add(label)

                adapter.notifyDataSetChanged()
                binding.addLabel.setText("")
                addLabel(label)
            }
        }

        binding.addTextlayout.setStartIconOnClickListener {
            if (!isOpen) {
                binding.colorView.visibility = View.VISIBLE
                isOpen = true
            } else {
                binding.colorView.visibility = View.GONE
                isOpen = false
            }
        }

        // Adapter OnLongClick Listener
        // TODO: 22/12/2020 give option to delete all task or only label (which means also labelling)
        adapter.deleteListener = {  deleteLabel(it)  }

        adapter.editListener = { updateLabel(it) }


        colorAdapter.listener = { color ->
            this.color = color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.addTextlayout.startIconDrawable?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
            } else {
                binding.addLabel.setTextColor(color)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        labelViewModel.getLabelWithTask().observe(this, { labels ->
            labels?.let {
                this.labels.clear()
                this.labels.addAll(it)
            }
            adapter.notifyDataSetChanged()
        })
    }

    private fun addLabel(label: Label){ labelViewModel.insert(label); hideKeyboard()}

    private fun updateLabel(label: Label){ labelViewModel.update(label); hideKeyboard() }

    private fun deleteLabel(label: Label) {labelViewModel.delete(label)}

    private fun hideKeyboard(){
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LabelFragment.
         */
        @JvmStatic
        fun newInstance() = LabelFragment()
    }
}