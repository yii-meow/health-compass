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
import com.example.healthcompass.data.Friend.FriendClass
import com.example.healthcompass.dataAdapter.FriendRequestAdapter
import com.example.healthcompass.databinding.FragmentFriendRequestBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendRequestFragment : Fragment() {
    private var _binding : FragmentFriendRequestBinding? = null
    private val binding get() = _binding!!
    private lateinit var database : DatabaseReference
    private lateinit var friendRequestList : MutableList<FriendClass>
    private lateinit var adapter : FriendRequestAdapter
    private lateinit var currentUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendRequestBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = getUsername()
        checkHasUsername(user)
        currentUser = user.toString()
        setupAdapter()
        loadFriendRequests()
    }

    private fun loadFriendRequests() {
        val friendRequestRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("Friend Request")

        friendRequestList.clear()

        friendRequestRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (requestSnapshot in snapshot.children) {
                    val username = requestSnapshot.key
                    if (username != null) {
                        loadUserDetails(username)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadUserDetails(username: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(username)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("Profile Information").child("name").getValue(String::class.java)
                    val gender = snapshot.child("Profile Information").child("gender").getValue(String::class.java)
                    val friend = FriendClass(username, name ?: "", gender ?: "")
                    friendRequestList.add(friend)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupAdapter() {
        friendRequestList = mutableListOf()
        adapter = FriendRequestAdapter(requireContext(), friendRequestList, currentUser)
        binding.gridFriendRequest.adapter = adapter
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