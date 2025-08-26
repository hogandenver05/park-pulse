package com.denverhogan.themeparks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.denverhogan.themeparks.databinding.FragmentThemeParksListBinding
import com.denverhogan.themeparks.model.ThemePark

class ThemeParksListFragment : Fragment() {
    private var _binding: FragmentThemeParksListBinding? = null
    private val binding get() = _binding!!

    private val dataSet = (1..100).map {
        ThemePark(
            name = "Kings Island",
            location = "Mason, OH"
        )
    }

    private val adapter = ThemeParksAdapter(
        dataSet = dataSet,
        onItemClick = { onItemClick() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeParksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.parksListRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.parksListRecyclerView.adapter = adapter
    }

    private fun onItemClick() {
        Log.d(LOG_TAG, "Button clicked!")
        Log.e(LOG_TAG, "Button clicked!")

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, ThemeParkDetailFragment.newInstance(id = 1))
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOG_TAG = "ThemeParksListFragment"

        @JvmStatic
        fun newInstance() = ThemeParksListFragment()
    }
}