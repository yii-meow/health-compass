package com.example.healthcompass.ui.achievements

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.healthcompass.R
import com.example.healthcompass.data.Nutrition.UserViewModel
import com.example.healthcompass.data.User.UserClass
import com.google.firebase.database.DatabaseError

class achievements_milestones_list : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var lblBadminton: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_achievements_milestones_list, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        updateMilestone()

        return view
    }

    private fun updateMilestone() {
        userViewModel.fetchUserDetails(object : UserViewModel.OnRequestCompleteCallBack<UserClass> {
            override fun onSuccess(list: List<UserClass>) {
                val milestonesMap = list[0].milestones
                val milestones = mapToMilestone(milestonesMap)
                updateMilestoneUI(milestones)
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun mapToMilestone(map: Map<String, Map<String, Boolean>>): Milestone {
        return Milestone(
            badminton = map["badminton"] ?: emptyMap(),
            running = map["running"] ?: emptyMap(),
            walking = map["walking"] ?: emptyMap()
        )
    }

    private fun updateMilestoneUI(milestone: Milestone) {
        milestone.badminton["3_times"]?.let {
            if (it) view?.findViewById<TextView>(R.id.badgeBadminton3TimesCompleted)?.visibility =
                View.VISIBLE
        }
        milestone.badminton["7_times"]?.let {
            if (it) view?.findViewById<TextView>(R.id.badgeBadminton7TimesCompleted)?.visibility =
                View.VISIBLE
        }
        milestone.badminton["30_times"]?.let {
            if (it) view?.findViewById<TextView>(R.id.badgeBadminton30TimesCompleted)?.visibility =
                View.VISIBLE
        }
        milestone.running["3_times"]?.let {
            if (it) view?.findViewById<TextView>(R.id.badgeRunning3TimesCompleted)?.visibility =
                View.VISIBLE
        }
        milestone.running["25_times"]?.let {
            if (it) view?.findViewById<TextView>(R.id.badgeRunning25TimesCompleted)?.visibility =
                View.VISIBLE
        }
        milestone.running["50_times"]?.let {
            if (it) view?.findViewById<TextView>(R.id.badgeRunning50TimesCompleted)?.visibility =
                View.VISIBLE
        }
        milestone.walking["3_times"]?.let {
            if (it) view?.findViewById<TextView>(R.id.badgeWalking3TimesCompleted)?.visibility =
                View.VISIBLE
        }
        milestone.walking["25_times"]?.let {
            if (it) view?.findViewById<TextView>(R.id.badgeWalking25TimesCompleted)?.visibility =
                View.VISIBLE
        }
        milestone.walking["50_times"]?.let {
            if (it) view?.findViewById<TextView>(R.id.badgeWalking50TimesCompleted)?.visibility =
                View.VISIBLE
        }
    }

    data class Milestone(
        val badminton: Map<String, Boolean> = emptyMap(),
        val running: Map<String, Boolean> = emptyMap(),
        val walking: Map<String, Boolean> = emptyMap()
    )
}