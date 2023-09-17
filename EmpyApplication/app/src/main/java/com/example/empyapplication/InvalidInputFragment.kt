package com.example.empyapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentContainerView


/**
 * When the user does not input a valid guess, they are taken here
 */
    class InvalidInputFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val tmp = inflater.inflate(R.layout.fragment_invalid_input, container, false)

        val okayButton = tmp.findViewById<Button>(R.id.okay)
        okayButton.setOnClickListener {
            val parentActivity = requireActivity() as MainActivity
            parentActivity.gotoGame()
        }

        return tmp
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment InvalidInputFragment.
         */
        @JvmStatic
        fun newInstance() =
            InvalidInputFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}