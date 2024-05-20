package com.example.healthcompass.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthcompass.R
import com.example.healthcompass.data.Nutrition.UserViewModel
import com.example.healthcompass.data.User.UserClass
import com.google.firebase.database.DatabaseError

class achievements_milestones_list : Fragment() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_achievements_milestones_list, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        updateMilestone()

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

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
        val action =
            achievements_milestones_listDirections.actionAchievementsMilestonesListToAchievementsMilestonesDetails2()

        milestone.badminton["3_times"]?.let { isCompleted ->
            if (isCompleted) {
                view?.findViewById<TextView>(R.id.badgeBadminton3TimesCompleted)?.visibility =
                    View.VISIBLE
                view?.findViewById<FrameLayout>(R.id.badgeBadminton3Times)?.isClickable =
                    true

                view?.findViewById<FrameLayout>(R.id.badgeBadminton3Times)?.setOnClickListener {
                    action.title = "Badminton"
                    action.desc = "3 Times"

                    findNavController().navigate(action)
                }
            }
        }

        milestone.badminton["7_times"]?.let { isCompleted ->
            if (isCompleted) {
                Toast.makeText(
                    requireContext(),
                    "${milestone.badminton["7_times"]}",
                    Toast.LENGTH_LONG
                )
                    .show()

                view?.findViewById<TextView>(R.id.badgeBadminton7TimesCompleted)?.visibility =
                    View.VISIBLE
                view?.findViewById<FrameLayout>(R.id.badgeBadminton7Times)?.isClickable =
                    true

                view?.findViewById<FrameLayout>(R.id.badgeBadminton7Times)?.setOnClickListener {
                    action.title = "Badminton"
                    action.desc = "7 Times"

                    findNavController().navigate(action)
                }
            }
        }
        milestone.badminton["30_times"]?.let { isCompleted ->
            if (isCompleted) {
                view?.findViewById<TextView>(R.id.badgeBadminton30TimesCompleted)?.visibility =
                    View.VISIBLE
                view?.findViewById<FrameLayout>(R.id.badgeBadminton30Times)?.isClickable =
                    true
                view?.findViewById<FrameLayout>(R.id.badgeBadminton30Times)?.setOnClickListener {
                    action.title = "Badminton"
                    action.desc = "30 Times"

                    findNavController().navigate(action)
                }
            }
        }
        milestone.running["3_times"]?.let { isCompleted ->
            if (isCompleted) {
                view?.findViewById<TextView>(R.id.badgeRunning3TimesCompleted)?.visibility =
                    View.VISIBLE
                view?.findViewById<FrameLayout>(R.id.badgeRunning3Times)?.isClickable =
                    true
                view?.findViewById<FrameLayout>(R.id.badgeRunning3Times)?.setOnClickListener {
                    action.title = "Running"
                    action.desc = "3 Times"

                    findNavController().navigate(action)
                }
            }
        }
        milestone.running["25_times"]?.let { isCompleted ->
            if (isCompleted) {
                view?.findViewById<TextView>(R.id.badgeRunning25TimesCompleted)?.visibility =
                    View.VISIBLE
                view?.findViewById<FrameLayout>(R.id.badgeRunning25Times)?.isClickable =
                    true
                view?.findViewById<FrameLayout>(R.id.badgeRunning25Times)?.setOnClickListener {
                    action.title = "Running"
                    action.desc = "25 Times"

                    findNavController().navigate(action)
                }
            }
        }
        milestone.running["50_times"]?.let { isCompleted ->
            if (isCompleted) {
                view?.findViewById<TextView>(R.id.badgeRunning50TimesCompleted)?.visibility =
                    View.VISIBLE
                view?.findViewById<FrameLayout>(R.id.badgeRunning50Times)?.isClickable =
                    true
                view?.findViewById<FrameLayout>(R.id.badgeRunning50Times)?.setOnClickListener {
                    action.title = "Running"
                    action.desc = "50 Times"

                    findNavController().navigate(action)
                }
            }
        }
        milestone.walking["3_times"]?.let { isCompleted ->
            if (isCompleted) {
                view?.findViewById<TextView>(R.id.badgeWalking3TimesCompleted)?.visibility =
                    View.VISIBLE
                view?.findViewById<FrameLayout>(R.id.badgeWalking3Times)?.isClickable =
                    true
                view?.findViewById<FrameLayout>(R.id.badgeWalking3Times)?.setOnClickListener {
                    action.title = "Walking"
                    action.desc = "3 Times"

                    findNavController().navigate(action)
                }
            }
        }
        milestone.walking["25_times"]?.let { isCompleted ->
            if (isCompleted) {
                view?.findViewById<TextView>(R.id.badgeWalking25TimesCompleted)?.visibility =
                    View.VISIBLE
                view?.findViewById<FrameLayout>(R.id.badgeWalking25Times)?.isClickable =
                    true
                view?.findViewById<FrameLayout>(R.id.badgeWalking25Times)?.setOnClickListener {
                    action.title = "Walking"
                    action.desc = "25 Times"

                    findNavController().navigate(action)
                }
            }
        }
        milestone.walking["50_times"]?.let { isCompleted ->
            if (isCompleted) {
                view?.findViewById<TextView>(R.id.badgeWalking50TimesCompleted)?.visibility =
                    View.VISIBLE
                view?.findViewById<FrameLayout>(R.id.badgeWalking50Times)?.isClickable =
                    true
                view?.findViewById<FrameLayout>(R.id.badgeWalking50Times)?.setOnClickListener {
                    action.title = "Walking"
                    action.desc = "50 Times"

                    findNavController().navigate(action)
                }
            }
        }
    }

    data class Milestone(
        val badminton: Map<String, Boolean> = emptyMap(),
        val running: Map<String, Boolean> = emptyMap(),
        val walking: Map<String, Boolean> = emptyMap()
    )
}