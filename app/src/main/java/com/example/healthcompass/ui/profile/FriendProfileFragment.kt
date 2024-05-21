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
import com.example.healthcompass.databinding.FragmentFriendProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendProfileFragment : Fragment() {
    private var _binding : FragmentFriendProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = getUsername()
        checkHasUsername(user)

        val args = arguments
        val friendUsername = args?.getString("friendUsername")
        displayProfileInfo(friendUsername)
    }

    private fun displayProfileInfo(friendUsername : String?) {
        val username : String = friendUsername.toString()

        database = FirebaseDatabase.getInstance().getReference("Users")

        database.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {
                if (snapshot.exists()) {
                    val nameDb : String? = snapshot.child("Profile Information").child("name").getValue(String::class.java)
                    val ageDb : Int? = snapshot.child("Profile Information").child("age").getValue(Int::class.java)
                    val genderDb : String? = snapshot.child("Profile Information").child("gender").getValue(String::class.java)
                    val heightDb : Float? = snapshot.child("Profile Information").child("height").getValue(Float::class.java)
                    val weightDb : Float? = snapshot.child("Profile Information").child("weight").getValue(Float::class.java)

                    if (genderDb == "Male") {
                        binding.imgProfileFriendProfile.setImageResource(R.drawable.male)
                    } else {
                        binding.imgProfileFriendProfile.setImageResource(R.drawable.female)
                    }

                    binding.txtFriendProfileName.text = nameDb.toString()
                    binding.txtFriendProfileUsername.text = username
                    binding.txtFriendProfileAge.text = ageDb.toString()
                    binding.txtFriendProfileHeight.text = heightDb.toString()
                    binding.txtFriendProfileWeight.text = weightDb.toString()
                } else {
                    val checkUser = getUsername()
                    checkHasUsername(checkUser)
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