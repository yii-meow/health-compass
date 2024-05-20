package com.example.healthcompass.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.healthcompass.dataAdapter.VpAdapter
import com.example.healthcompass.databinding.FragmentFriendsBinding
import com.google.android.material.tabs.TabLayoutMediator

class FriendsFragment : Fragment() {
    private var _binding : FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private val tabTitles = arrayOf("Friend List", "Add Friend", "Friend Request")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)

        val user = getUsername()
        checkHasUsername(user)
        setupViewPagerAndTabs()

        binding.backArrFriends.setOnClickListener{ back() }

        return binding.root
    }

    private fun back() {
        parentFragmentManager.popBackStack()
    }

    private fun setupViewPagerAndTabs() {
        val adapter = VpAdapter(requireActivity())
        binding.friendsContainer.adapter = adapter

        TabLayoutMediator(binding.friendsTab, binding.friendsContainer) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        binding.friendsContainer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvFriendsTitle.text = tabTitles[position]
            }
        })
    }

    private fun checkHasUsername(user : String?) {
        if (user == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUsername() : String? {
        val sharedPref : SharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}
