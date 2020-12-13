package com.project.thisappistryingtomakeyoubetter.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.thisappistryingtomakeyoubetter.R
import com.project.thisappistryingtomakeyoubetter.databinding.FragmentLabelBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LabelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LabelFragment : Fragment() {

    private lateinit var binding: FragmentLabelBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLabelBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LabelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = LabelFragment()
    }
}