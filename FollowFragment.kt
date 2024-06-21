package org.d3if3077.githubapps.ui.description

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if3077.githubapps.ui.FollowUserAdapter
import org.d3if3077.githubapps.ui.ViewModelFactory
import org.d3if3077.githubapps.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    private var position = 0
    var username: String = ""

    private lateinit var binding: FragmentFollowBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel = obtainDetailViewModel(requireActivity())

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) ?: "ramrambgr"
        }

        detailViewModel.getFollowings(username)
        detailViewModel.getFollowers(username)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager

        detailViewModel.loading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        if (position == 1) {
            detailViewModel.userFollowers.observe(viewLifecycleOwner) {
                val adapter = FollowUserAdapter()
                adapter.submitList(it)
                binding.rvFollow.adapter = adapter
            }
        } else {
            detailViewModel.userFollowings.observe(viewLifecycleOwner) {
                val adapter = FollowUserAdapter()
                adapter.submitList(it)
                binding.rvFollow.adapter = adapter
            }
        }
    }

    private fun obtainDetailViewModel(activity: FragmentActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(requireActivity(), factory)[DetailViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBarFollow.visibility = View.VISIBLE
            } else {
                progressBarFollow.visibility = View.GONE
            }
        }
    }

    companion object {
        const val ARG_USERNAME = "0"
        const val ARG_POSITION = "auditanaviraearly"
    }
}