package com.example.healthcompass.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.healthcompass.data.Friend.FriendClass
import com.example.healthcompass.data.Friend.FriendListViewModel
import com.example.healthcompass.dataAdapter.FriendListAdapter
import com.example.healthcompass.databinding.FragmentFriendListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendListFragment : Fragment() {
    private var _binding : FragmentFriendListBinding? = null
    private val binding get() = _binding!!
    private lateinit var database : DatabaseReference
    private lateinit var viewModel : FriendListViewModel
    private lateinit var friendList : MutableList<FriendClass>
    private lateinit var adapter : FriendListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = getUsername()
        checkHasUsername(user)

        viewModel = ViewModelProvider(this).get(FriendListViewModel::class.java)
        friendList = mutableListOf()
        adapter = FriendListAdapter(requireContext(), friendList)

        binding.gridFriendList.adapter = adapter

        loadFriendsFromFirebase(user)
        observeFriendList()
    }

    private fun observeFriendList() {
        viewModel.friendList.observe(viewLifecycleOwner) { friends ->
            friendList.clear()
            friendList.addAll(friends)
            adapter.notifyDataSetChanged()
        }
    }

    private fun loadFriendsFromFirebase(user : String?) {
        val username = user.toString()

        database = FirebaseDatabase.getInstance().getReference("Users").child(username).child("Friends")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedFriends = mutableListOf<FriendClass>()
                for (data in snapshot.children) {
                    val friendUsername = data.key
                    friendUsername?.let { loadFriendDetails(it, loadedFriends) }
                }
                friendList.clear()
                friendList.addAll(loadedFriends)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadFriendDetails(friendUsername : String, loadedFriends : MutableList<FriendClass>) {
        val friendRef = FirebaseDatabase.getInstance().getReference("Users").child(friendUsername).child("Profile Information")
        friendRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    val gender = snapshot.child("gender").getValue(String::class.java)
                    val friend = FriendClass(friendUsername, name ?: "", gender ?: "")
                    friendList.add(friend)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
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