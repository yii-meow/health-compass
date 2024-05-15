package com.example.healthcompass.dataAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcompass.R
import com.example.healthcompass.data.FitnessActivity.FitnessActivity

class FitnessListAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FitnessListAdapter.MyViewHolder>() {
    private var fitnessList = emptyList<FitnessActivity>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val tvActivity: TextView = itemView.findViewById(R.id.tvActivity)
        val tvActivityTime: TextView = itemView.findViewById(R.id.tvActivityTime)
        val tvActivityDuration: TextView = itemView.findViewById(R.id.tvActivityDuration)
        val tvActivityCalories: TextView = itemView.findViewById(R.id.tvActivityCalories)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                listener.itemClick(position)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FitnessListAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.fitness_view_holder, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FitnessListAdapter.MyViewHolder, position: Int) {
        val currentItem = fitnessList[position]

        holder.tvActivity.text = currentItem.activityName
        holder.tvActivityTime.text = currentItem.activityDate
        holder.tvActivityDuration.text = currentItem.duration
        holder.tvActivityCalories.text = currentItem.caloriesBurnt.toString()
    }

    override fun getItemCount(): Int {
        return fitnessList.size
    }

    fun setData(fitnessActivityList: List<FitnessActivity>) {
        this.fitnessList = fitnessActivityList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun itemClick(position: Int)
    }
}