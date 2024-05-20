package com.example.healthcompass.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.example.healthcompass.R
import com.example.healthcompass.databinding.FragmentManageProfileInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ManageProfileInfoFragment : Fragment() {
    private var _binding : FragmentManageProfileInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageProfileInfoBinding.inflate(inflater, container, false)

        val user = getUsername()
        checkHasUsername(user)
        displayProfileInfo(user)

        binding.backArrManageProfileInfo.setOnClickListener{ back() }
        binding.btnManageCancel.setOnClickListener { back() }
        binding.btnManageSave.setOnClickListener { save(user) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.spnGender, android.R.layout.simple_spinner_item)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnManageGender.adapter = genderAdapter
    }

    private fun save(user : String?) {
        val username : String = user.toString()

        database = FirebaseDatabase.getInstance().getReference("Users")

        database.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (checkName() && checkAge() && checkGender() && checkHeight() && checkWeight()) {
                        val name : String = binding.txtManageName.text.toString()
                        val age : Int = binding.txtManageAge.text.toString().toInt()
                        val gender : String = binding.spnManageGender.selectedItem.toString()
                        val height : Float = binding.txtManageHeight.text.toString().toFloat()
                        val weight : Float = binding.txtManageWeight.text.toString().toFloat()

                        val userClass = mapOf<String, Any>("name" to name, "gender" to gender, "age" to age, "height" to height, "weight" to weight)
                        database = FirebaseDatabase.getInstance().getReference("Users")
                        database.child(username).child("Profile Information").updateChildren(userClass)

                        back()
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

    private fun checkName() : Boolean {
        val name : String = binding.txtManageName.text.toString()

        if (name.isEmpty()) {
            binding.txtManageName.error = "Name cannot be empty"
            binding.txtManageName.requestFocus()
            return false
        }

        if (name.any { it.isDigit() }) {
            binding.txtManageName.error = "Name cannot have digits"
            binding.txtManageName.requestFocus()
            return false
        }

        return true
    }

    private fun checkAge() : Boolean {
        val ageString : String = binding.txtManageAge.text.toString()

        if (ageString.isEmpty()) {
            binding.txtManageAge.error = "Age cannot be empty"
            binding.txtManageAge.requestFocus()
            return false
        }

        val age : Int = binding.txtManageAge.text.toString().toInt()

        if (age <= 0) {
            binding.txtManageAge.error = "Age must larger than 0"
            binding.txtManageAge.requestFocus()
            return false
        }

        return true
    }

    private fun checkGender() : Boolean {
        val gender : String = binding.spnManageGender.selectedItem.toString()

        if (gender == "Gender") {
            (binding.spnManageGender.selectedView as? TextView)?.error = "Select a gender"
            binding.spnManageGender.requestFocus()
            return false
        }

        return true
    }

    private fun checkHeight() : Boolean {
        val heightString : String = binding.txtManageHeight.text.toString()

        if (heightString.isEmpty()) {
            binding.txtManageHeight.error = "Height cannot be empty"
            binding.txtManageHeight.requestFocus()
            return false
        }

        val height : Float = binding.txtManageHeight.text.toString().toFloat()

        if (height <= 0) {
            binding.txtManageHeight.error = "Height must larger than 0"
            binding.txtManageHeight.requestFocus()
            return false
        }

        return true
    }

    private fun checkWeight() : Boolean {
        val weightString : String = binding.txtManageWeight.text.toString()

        if (weightString.isEmpty()) {
            binding.txtManageWeight.error = "Weight cannot be empty"
            binding.txtManageWeight.requestFocus()
            return false
        }

        val weight : Float = binding.txtManageWeight.text.toString().toFloat()

        if (weight <= 0) {
            binding.txtManageWeight.error = "Weight must larger than 0"
            binding.txtManageWeight.requestFocus()
            return false
        }

        return true
    }

    private fun back() {
        parentFragmentManager.popBackStack()
    }

    private fun displayProfileInfo(user : String?) {
        val username : String = user.toString()

        database = FirebaseDatabase.getInstance().getReference("Users")

        database.child(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot : DataSnapshot) {
                if (snapshot.exists()) {
                    val nameDb : String? = snapshot.child("Profile Information").child("name").getValue(String::class.java)
                    val ageDb : Int? = snapshot.child("Profile Information").child("age").getValue(Int::class.java)
                    val genderDb : String? = snapshot.child("Profile Information").child("gender").getValue(String::class.java)
                    val heightDb : Float? = snapshot.child("Profile Information").child("height").getValue(Float::class.java)
                    val weightDb : Float? = snapshot.child("Profile Information").child("weight").getValue(Float::class.java)

                    val genderAdapter = binding.spnManageGender.adapter as ArrayAdapter<String>
                    val genderPosition = genderAdapter.getPosition(genderDb)

                    binding.txtManageName.setText(nameDb.toString())
                    binding.txtManageAge.setText(ageDb.toString())
                    binding.spnManageGender.setSelection(genderPosition)
                    binding.txtManageHeight.setText(heightDb.toString())
                    binding.txtManageWeight.setText(weightDb.toString())
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