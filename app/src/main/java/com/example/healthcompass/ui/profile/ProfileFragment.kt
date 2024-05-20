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
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R
import com.example.healthcompass.databinding.FragmentProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val user = getUsername()
        checkHasUsername(user)
        displayProfileInfo(user)

        binding.arrManageProfileInfo.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_manageProfileInfoFragment) }
        binding.arrAchievements.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_achievements_milestones_list) }
        binding.arrNotifications.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_notificationsFragment) }
        binding.arrFriends.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_friendsFragment) }
        binding.arrChangePassword.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment) }
        binding.btnLogOut.setOnClickListener { logOut() }

        return binding.root
    }

    private fun logOut() {
        val sharedPref: SharedPreferences =
            requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", null)
        editor.apply()

        val user = getUsername()
        checkHasUsername(user)
    }

    private fun displayProfileInfo(user: String?) {
        val username: String = user.toString()

        database = FirebaseDatabase.getInstance().getReference("Users")

        database.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val nameDb: String? = snapshot.child("Profile Information").child("name")
                        .getValue(String::class.java)
                    val ageDb: Int? =
                        snapshot.child("Profile Information").child("age").getValue(Int::class.java)
                    val genderDb: String? = snapshot.child("Profile Information").child("gender")
                        .getValue(String::class.java)
                    val heightDb: Float? = snapshot.child("Profile Information").child("height")
                        .getValue(Float::class.java)
                    val weightDb: Float? = snapshot.child("Profile Information").child("weight")
                        .getValue(Float::class.java)

                    if (genderDb == "Male") {
                        binding.imgProfile.setImageResource(R.drawable.male)
                    } else {
                        binding.imgProfile.setImageResource(R.drawable.female)
                    }

                    binding.txtProfileName.text = nameDb.toString()
                    binding.txtProfileUsername.text = username
                    binding.txtProfileAge.text = ageDb.toString()
                    binding.txtProfileHeight.text = heightDb.toString()
                    binding.txtProfileWeight.text = weightDb.toString()
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

    private fun checkHasUsername(user: String?) {
        if (user == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUsername(): String? {
        val sharedPref: SharedPreferences =
            requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }
}