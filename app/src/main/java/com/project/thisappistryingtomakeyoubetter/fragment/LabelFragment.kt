package com.project.thisappistryingtomakeyoubetter.fragment

import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.adapter.ColorAdapter
import com.project.thisappistryingtomakeyoubetter.adapter.LabelAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentLabelBinding
import com.project.thisappistryingtomakeyoubetter.model.Label

/**
 * A simple [Fragment] subclass.
 * Use the [LabelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LabelFragment : Fragment() {

    private val TAG = "LabelFragment"
    private lateinit var binding: FragmentLabelBinding
    private var labels: MutableList<Label> = ArrayList()
    private var isOpen = false
    private lateinit var label: Label
    private var color: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentLabelBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 14/12/2020  (5) Create Adapter (complete)
        // TODO: 14/12/2020  (6) Create dialog (skip)

        // Color Adapter
        val colorAdapter = ColorAdapter(requireContext(), resources.getIntArray(R.array.color_array).toList())
        binding.colorView.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false)
        binding.colorView.adapter = colorAdapter
        this.color = binding.addLabel.currentTextColor

        // Label Adapter
        val adapter = LabelAdapter(requireContext(), labels)
        binding.label.layoutManager = LinearLayoutManager(requireContext())
        binding.label.adapter = adapter

        // Click Listener
        binding.addTextlayout.setEndIconOnClickListener {
            if(binding.addLabel.text.toString() == ""){
                Toast.makeText(requireContext(),
                        getString(R.string.label_empty_notification),
                        Toast.LENGTH_LONG).show()
            } else {
                label = Label(0, binding.addLabel.text.toString(), this.color)
                labels.add(label)
                adapter.notifyDataSetChanged()
                binding.addLabel.setText("")
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


        colorAdapter.listener = {color ->
            Log.d(TAG, "onViewCreated: $color")
            this.color = color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                binding.addTextlayout.startIconDrawable?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
            } else {
                binding.addLabel.setTextColor(color)
            }
        }

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