package com.denverhogan.themeparks.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.denverhogan.themeparks.R
import com.denverhogan.themeparks.databinding.FragmentDestinationsListBinding
import com.denverhogan.themeparks.detail.DestinationDetailFragment
import com.denverhogan.themeparks.model.DestinationListItem
import com.denverhogan.themeparks.model.DestinationsListViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DestinationsListFragment : Fragment() {
    private var _binding: FragmentDestinationsListBinding? = null
    private val binding get() = _binding!!

    private val destinationsListViewModel: DestinationsListViewModel by viewModels()
    private lateinit var destinationsListAdapter: DestinationsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDestinationsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        destinationsListAdapter = DestinationsListAdapter(onItemClick = ::onItemClick)
        binding.destinationsListRecyclerView.adapter = destinationsListAdapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectViewState()
            }
        }
    }

    private suspend fun collectViewState() {
        destinationsListViewModel.viewState.collect { viewState ->
            when (viewState) {
                is DestinationsListViewState.Error -> TODO()
                DestinationsListViewState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.destinationsListRecyclerView.isVisible = false
                }

                is DestinationsListViewState.Success -> {
                    destinationsListAdapter.updateData(viewState.destinations)
                    binding.progressBar.isVisible = false
                    binding.destinationsListRecyclerView.isVisible = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onItemClick(item: DestinationListItem) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container,
                DestinationDetailFragment.newInstance(id = 0, name = item.name) //TODO: fix ID logic
            )
            addToBackStack(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DestinationsListFragment()
    }
}