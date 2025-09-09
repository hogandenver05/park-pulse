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
import androidx.recyclerview.widget.DividerItemDecoration
import com.denverhogan.themeparks.R
import com.denverhogan.themeparks.databinding.FragmentRidesListBinding
import com.denverhogan.themeparks.detail.RideDetailFragment
import com.denverhogan.themeparks.model.RidesListViewState
import com.denverhogan.themeparks.model.Ride
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RidesListFragment : Fragment() {
    private var _binding: FragmentRidesListBinding? = null
    private val binding get() = _binding!!

    private val destinationsListViewModel: RidesListViewModel by viewModels()
    private lateinit var ridesListAdapter: RidesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRidesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ridesListAdapter = RidesListAdapter(onItemClick = ::onItemClick)
        binding.ridesListRecyclerView.adapter = ridesListAdapter
        binding.ridesListRecyclerView.addItemDecoration(DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL))

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectViewState()
            }
        }
    }

    private suspend fun collectViewState() {
        destinationsListViewModel.viewState.collect { viewState ->
            when (viewState) {
                is RidesListViewState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.ridesListRecyclerView.isVisible = false
                    binding.errorMessage.isVisible = false
                }
                is RidesListViewState.Error -> {
                    binding.errorMessage.text = viewState.errorMessage
                    binding.progressBar.isVisible = false
                    binding.ridesListRecyclerView.isVisible = false
                    binding.errorMessage.isVisible = true
                }
                is RidesListViewState.Success -> {
                    ridesListAdapter.updateData(viewState.rides)
                    binding.progressBar.isVisible = false
                    binding.ridesListRecyclerView.isVisible = true
                    binding.errorMessage.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onItemClick(item: Ride) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fragment_container, RideDetailFragment.newInstance(
                    id = item.id, name = item.name
                )
            )
            addToBackStack(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = RidesListFragment()
    }
}