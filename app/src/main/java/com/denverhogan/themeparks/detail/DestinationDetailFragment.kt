package com.denverhogan.themeparks.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.denverhogan.themeparks.databinding.FragmentDestinationDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DestinationDetailFragment : Fragment() {
    private var _binding: FragmentDestinationDetailBinding? = null
    private val binding get() = _binding!!

    private var id: Int? = null
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            id = it.getInt(ARG_ID)
            name = it.getString(ARG_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDestinationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.destinationName.text = name ?: "An error occurred"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ID = "id"
        private const val ARG_NAME = "name"

        @JvmStatic
        fun newInstance(id: Long, name: String) = DestinationDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_ID, id)
                putString(ARG_NAME, name)
            }
        }
    }
}