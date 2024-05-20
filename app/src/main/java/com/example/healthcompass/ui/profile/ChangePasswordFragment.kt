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
import com.example.healthcompass.databinding.FragmentChangePasswordBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChangePasswordFragment : Fragment() {
    private var _binding : FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        val user = getUsername()
        checkHasUsername(user)

        binding.backArrChangePassword.setOnClickListener{ back() }
        binding.btnChangeCancel.setOnClickListener { back() }
        binding.btnChangeUpdate.setOnClickListener { update(user) }

        return binding.root
    }

    private fun update(user : String?) {
        val username : String = user.toString()

        database = FirebaseDatabase.getInstance().getReference("Users")

        database.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val oldPassword : String = binding.txtOldPassword.text.toString().trim()
                    val passwordDb : String = snapshot.child("Profile Information").child("password").getValue(String::class.java).toString()

                    if (oldPassword.isNotEmpty()) {
                        if (oldPassword == passwordDb) {
                            if (checkPassword()) {
                                val newPassword : String = binding.txtNewPassword.text.toString().trim()

                                val userClass = mapOf<String, Any>("password" to newPassword)
                                database = FirebaseDatabase.getInstance().getReference("Users")
                                database.child(username).child("Profile Information").updateChildren(userClass)

                                back()
                            }
                        } else {
                            binding.txtOldPassword.error = "Password invalid"
                            binding.txtOldPassword.requestFocus()
                        }
                    } else {
                        binding.txtOldPassword.error = "Password cannot be empty"
                        binding.txtOldPassword.requestFocus()
                    }
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

    private fun checkPassword() : Boolean {
        val password : String = binding.txtNewPassword.text.toString().trim()
        val confirmPassword : String = binding.txtConfirmNewPassword.text.toString().trim()

        var hasChar = false
        var hasDigit = false
        var hasSpecialChar = false

        if (password.isEmpty()) {
            binding.txtNewPassword.error = "Password cannot be empty"
            binding.txtNewPassword.requestFocus()
            return false
        }

        if (password.length < 8) {
            binding.txtNewPassword.error = "Password length must at least 8"
            binding.txtNewPassword.requestFocus()
            return false
        }

        for (char in password) {
            if (char.isLetter()) {
                hasChar = true
            } else if (char.isDigit()) {
                hasDigit = true
            } else if (char in "!@#$%^&*()_+-={}[]|\\:;\"'<>,.?/") {
                hasSpecialChar = true
            }
        }

        if (!(hasChar) || !(hasDigit) || !(hasSpecialChar)) {
            binding.txtNewPassword.error = "Password must contains at least 1 character, 1 digit, and 1 special character"
            binding.txtNewPassword.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            binding.txtConfirmNewPassword.error = "Confirm password cannot be empty"
            binding.txtConfirmNewPassword.requestFocus()
            return false
        }

        if (confirmPassword != password) {
            binding.txtConfirmNewPassword.error = "Confirm password must be same with password"
            binding.txtConfirmNewPassword.requestFocus()
            return false
        }

        return true
    }

    private fun back() {
        parentFragmentManager.popBackStack()
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