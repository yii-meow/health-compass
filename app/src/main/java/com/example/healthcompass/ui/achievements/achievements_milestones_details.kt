package com.example.healthcompass.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R

class achievements_milestones_details : Fragment() {
    private val args by navArgs<achievements_milestones_detailsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_achievements_milestones_details, container, false)

        val tvBadgeTitle : TextView = view.findViewById(R.id.tvBadgeTitle)
        val tvBadgeDesc : TextView = view.findViewById(R.id.tvBadgeDesc)

        tvBadgeTitle.text = args.title
        tvBadgeDesc.text = args.desc

        return view
    }
}