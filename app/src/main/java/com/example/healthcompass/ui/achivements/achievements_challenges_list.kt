package com.example.healthcompass.ui.achivements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthcompass.R

/**
 * A simple [Fragment] subclass.
 * Use the [achievements_challenges_list.newInstance] factory method to
 * create an instance of this fragment.
 */
class achievements_challenges_list : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_achievements_challenges_list, container, false)
    }
}