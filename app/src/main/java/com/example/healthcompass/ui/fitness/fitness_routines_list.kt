package com.example.healthcompass.ui.fitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcompass.R
import com.example.healthcompass.data.FitnessActivity.FitnessActivity
import com.example.healthcompass.data.FitnessActivity.FitnessActivityViewModel
import com.example.healthcompass.data.FitnessActivity.OnCaloriesCalculationCallback
import com.example.healthcompass.data.FitnessActivity.OnRequestCompleteCallBack
import com.example.healthcompass.dataAdapter.FitnessListAdapter
import com.google.firebase.database.DatabaseError

class fitness_routines_list : Fragment(), FitnessListAdapter.OnItemClickListener {
    private lateinit var viewModel: FitnessActivityViewModel
    private lateinit var fitnessActivityList: List<FitnessActivity>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fitness_routines_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.fitness_recyclerView)

        val adapter = FitnessListAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        viewModel = ViewModelProvider(this).get(FitnessActivityViewModel::class.java)

        // Live Data
//        viewModel.getFitnessActivitiesLiveData()
//            .observe(viewLifecycleOwner, Observer { activities ->
//                adapter.submitList(activities)
//            })

        viewModel.getAllFitnessActivity(object : OnRequestCompleteCallBack {
            override fun onSuccess(list: List<FitnessActivity>) {
                fitnessActivityList = list
                adapter.setData(fitnessActivityList)
            }

            override fun onFailure(error: DatabaseError) {
                Toast.makeText(requireContext(), "$error", Toast.LENGTH_LONG).show()
            }
        })

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        return view
    }

    override fun itemClick(position: Int) {
        val fitnessActivity: FitnessActivity = fitnessActivityList[position]
        val action =
            fitness_routines_listDirections.actionFitnessRoutinesListToFitnessRoutinesDetails()
        action.fitnessDay = fitnessActivity.activityDate
        action.fitnessTime = fitnessActivity.startTime
        findNavController().navigate(action)
    }
}