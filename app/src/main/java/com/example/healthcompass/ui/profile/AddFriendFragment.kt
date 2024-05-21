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
import com.example.healthcompass.R
import com.example.healthcompass.databinding.FragmentAddFriendBinding
import com.example.healthcompass.data.Friend.FriendClass
import com.example.healthcompass.dataAdapter.AddFriendAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 * Use the [AddFriendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFriendFragment : Fragment() {
    private var _binding : FragmentAddFriendBinding? = null
    private val binding get() = _binding!!
    private lateinit var database : DatabaseReference
    private lateinit var userList : MutableList<FriendClass>
    private lateinit var adapter : AddFriendAdapter
    private lateinit var currentUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFriendBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = getUsername()
        checkHasUsername(user)
        currentUser = user.toString()
        setupAdapter()
        loadUsersNotInFriends()
    }

    private fun loadUsersNotInFriends() {
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")
        val friendsRef = usersRef.child(currentUser).child("Friends")
        val addFriendRef = usersRef.child(currentUser).child("Add Friend")

        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                for (userSnapshot in usersSnapshot.children) {
                    val username = userSnapshot.key

                    if (username != currentUser) {
                        friendsRef.child(username!!).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(friendsSnapshot: DataSnapshot) {
                                if (!friendsSnapshot.exists()) {
                                    addFriendRef.child(username).addListenerForSingleValueEvent(object :
                                        ValueEventListener {
                                        override fun onDataChange(addFriendSnapshot: DataSnapshot) {
                                            val name = userSnapshot.child("Profile Information").child("name").getValue(String::class.java)
                                            val gender = userSnapshot.child("Profile Information").child("gender").getValue(String::class.java)
                                            val user = FriendClass(username, name ?: "", gender ?: "")
                                            if (addFriendSnapshot.exists()) {
                                                user.status = "Pending"
                                            }
                                            userList.add(user)
                                            adapter.notifyDataSetChanged()
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupAdapter() {
        userList = mutableListOf()
        adapter = AddFriendAdapter(requireContext(), userList, currentUser)
        binding.gridAddFriend.adapter = adapter
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