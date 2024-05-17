package com.example.healthcompass.ui.nutrition

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

class edit_hydration : Fragment() {
    private var selectedCardId: Int? = null
    private var selectedSizeCardId: Int? = null
    private var selectedServingCardId: Int? = null
    private lateinit var dbRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_hydration, container, false)

        // Type of drinks
        val cardWater = view.findViewById<CardView>(R.id.cardWater)
        val cardCoffee = view.findViewById<CardView>(R.id.cardCoffee)
        val cardTea = view.findViewById<CardView>(R.id.cardTea)
        val cardAlcohol = view.findViewById<CardView>(R.id.cardAlcohol)

        cardWater.setOnClickListener { selectCard(R.id.cardWater) }
        cardCoffee.setOnClickListener { selectCard(R.id.cardCoffee) }
        cardTea.setOnClickListener { selectCard(R.id.cardTea) }
        cardAlcohol.setOnClickListener { selectCard(R.id.cardAlcohol) }

        // Sizes
        val card100ml = view.findViewById<CardView>(R.id.card100ml)
        val card250ml = view.findViewById<CardView>(R.id.card250ml)
        val card500ml = view.findViewById<CardView>(R.id.card500ml)
        val card1L = view.findViewById<CardView>(R.id.card1L)

        card100ml.setOnClickListener { selectSizeCard(R.id.card100ml) }
        card250ml.setOnClickListener { selectSizeCard(R.id.card250ml) }
        card500ml.setOnClickListener { selectSizeCard(R.id.card500ml) }
        card1L.setOnClickListener { selectSizeCard(R.id.card1L) }

        // Servings
        val cardServing1 = view.findViewById<CardView>(R.id.cardServing1)
        val cardServing2 = view.findViewById<CardView>(R.id.cardServing2)
        val cardServing3 = view.findViewById<CardView>(R.id.cardServing3)

        cardServing1.setOnClickListener { selectServingCard(R.id.cardServing1) }
        cardServing2.setOnClickListener { selectServingCard(R.id.cardServing2) }
        cardServing3.setOnClickListener { selectServingCard(R.id.cardServing3) }

        val btnDone: Button = view.findViewById(R.id.btnDone)
        btnDone.setOnClickListener {
            // Check if both size and servings are selected for all sections
            if (selectedSizeCardId == null || selectedServingCardId == null) {
                // If any section's card is not selected, return 0 as total water intake
                Toast.makeText(
                    requireContext(),
                    "Please select both size and servings for all sections",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val totalWaterIntake = calculateTotalWaterIntake()
                saveHydrationData(totalWaterIntake)
                findNavController().navigate(R.id.action_edit_hydration_to_nutrition)
            }
        }
        return view
    }
    private fun selectCard(cardId: Int) {
        selectedCardId = cardId

        val cardWater = requireView().findViewById<CardView>(R.id.cardWater)
        val cardCoffee = requireView().findViewById<CardView>(R.id.cardCoffee)
        val cardTea = requireView().findViewById<CardView>(R.id.cardTea)
        val cardAlcohol = requireView().findViewById<CardView>(R.id.cardAlcohol)

        val selectedBackgroundColor = Color.parseColor("#AF4A56")
        val unselectedBackgroundColor = Color.parseColor("#9F9F9F")

        cardWater.setCardBackgroundColor(if (cardId == R.id.cardWater) selectedBackgroundColor else unselectedBackgroundColor)
        cardCoffee.setCardBackgroundColor(if (cardId == R.id.cardCoffee) selectedBackgroundColor else unselectedBackgroundColor)
        cardTea.setCardBackgroundColor(if (cardId == R.id.cardTea) selectedBackgroundColor else unselectedBackgroundColor)
        cardAlcohol.setCardBackgroundColor(if (cardId == R.id.cardAlcohol) selectedBackgroundColor else unselectedBackgroundColor)
    }

    private fun selectSizeCard(cardId: Int) {
        selectedSizeCardId = cardId

        val card100ml = requireView().findViewById<CardView>(R.id.card100ml)
        val card250ml = requireView().findViewById<CardView>(R.id.card250ml)
        val card500ml = requireView().findViewById<CardView>(R.id.card500ml)
        val card1L = requireView().findViewById<CardView>(R.id.card1L)

        val selectedBackgroundColor = Color.parseColor("#AF4A56")
        val unselectedBackgroundColor = Color.parseColor("#9F9F9F")

        card100ml.setCardBackgroundColor(if (cardId == R.id.card100ml) selectedBackgroundColor else unselectedBackgroundColor)
        card250ml.setCardBackgroundColor(if (cardId == R.id.card250ml) selectedBackgroundColor else unselectedBackgroundColor)
        card500ml.setCardBackgroundColor(if (cardId == R.id.card500ml) selectedBackgroundColor else unselectedBackgroundColor)
        card1L.setCardBackgroundColor(if (cardId == R.id.card1L) selectedBackgroundColor else unselectedBackgroundColor)
    }

    private fun selectServingCard(cardId: Int) {
        selectedServingCardId = cardId

        // Get references to the CardViews
        val cardServing1 = requireView().findViewById<CardView>(R.id.cardServing1)
        val cardServing2 = requireView().findViewById<CardView>(R.id.cardServing2)
        val cardServing3 = requireView().findViewById<CardView>(R.id.cardServing3)

        // Define the background color for the selected and unselected states
        val selectedBackgroundColor = Color.parseColor("#AF4A56")
        val unselectedBackgroundColor = Color.parseColor("#9F9F9F")

        // Set background color for each card based on the selected state
        cardServing1.setCardBackgroundColor(if (cardId == R.id.cardServing1) selectedBackgroundColor else unselectedBackgroundColor)
        cardServing2.setCardBackgroundColor(if (cardId == R.id.cardServing2) selectedBackgroundColor else unselectedBackgroundColor)
        cardServing3.setCardBackgroundColor(if (cardId == R.id.cardServing3) selectedBackgroundColor else unselectedBackgroundColor)
    }

    private fun calculateTotalWaterIntake(): Int {
        // Define the sizes for each drink option in milliliters
        val sizes = mapOf(
            R.id.card100ml to 100,
            R.id.card250ml to 250,
            R.id.card500ml to 500,
            R.id.card1L to 1000
        )

        // Get the size selected by the user
        val selectedSize = sizes[selectedSizeCardId ?: return 0] ?: return 0

        // Get the number of servings selected by the user
        val servings = when (selectedServingCardId) {
            R.id.cardServing1 -> 1
            R.id.cardServing2 -> 2
            R.id.cardServing3 -> 3
            else -> 0
        }

        // Calculate the total water intake by multiplying the size by the number of servings
        return selectedSize * servings
    }

    private fun saveHydrationData(totalHydrationIntake: Int) {
        dbRef = FirebaseDatabase.getInstance().getReference("Meal")

        val name = "yiyi"
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val strDate: String = formatter.format(date)

        // Retrieve existing hydration data from Firebase
        dbRef.child(name).child(strDate).child("Hydration").get()
            .addOnSuccessListener { dataSnapshot ->
                // Check if the data exists
                if (dataSnapshot.exists()) {
                    // Retrieve the existing hydration value
                    val existingHydration = dataSnapshot.value as Long

                    // Calculate the new hydration intake by adding the existing value with the new value
                    val newHydration = existingHydration + totalHydrationIntake

                    // Update the hydration value in Firebase
                    dbRef.child(name).child(strDate).child("Hydration").setValue(newHydration)
                        .addOnCompleteListener {
                            Toast.makeText(
                                requireContext(),
                                "Added hydration record successfully!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Failed to add hydration!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                } else {
                    // If no existing data found, set the new hydration value directly
                    dbRef.child(name).child(strDate).child("Hydration")
                        .setValue(totalHydrationIntake)
                        .addOnCompleteListener {
                            Toast.makeText(
                                requireContext(),
                                "Added hydration record successfully!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Failed to add hydration!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Failed to retrieve hydration data!",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}