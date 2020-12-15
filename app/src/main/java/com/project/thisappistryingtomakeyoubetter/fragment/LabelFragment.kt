package com.project.thisappistryingtomakeyoubetter.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.adapter.ColorAdapter
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentLabelBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LabelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LabelFragment : Fragment() {

    private lateinit var binding: FragmentLabelBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentLabelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 14/12/2020  (5) Create Adapter (complete)
        // TODO: 14/12/2020  (6) Create dialog


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