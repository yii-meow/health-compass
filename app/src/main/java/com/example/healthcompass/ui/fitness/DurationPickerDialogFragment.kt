package com.example.healthcompass.ui.fitness

import android.app.AlertDialog
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.example.healthcompass.R

class DurationPickerDialogFragment : DialogFragment() {

    private lateinit var listener: OnDurationSetListener

    interface OnDurationSetListener {
        fun onDurationSet(hours: Int, minutes: Int, seconds: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_duration_picker, null)

        val hoursPicker = dialogView.findViewById<NumberPicker>(R.id.hoursPicker)
        val minutesPicker = dialogView.findViewById<NumberPicker>(R.id.minutesPicker)
        val secondsPicker = dialogView.findViewById<NumberPicker>(R.id.secondsPicker)

        // Initialize NumberPickers
        hoursPicker.minValue = 0
        hoursPicker.maxValue = 23

        minutesPicker.minValue = 0
        minutesPicker.maxValue = 59

        secondsPicker.minValue = 0
        secondsPicker.maxValue = 59

        return AlertDialog.Builder(requireContext())
            .setTitle("Select Duration")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val hours = hoursPicker.value
                val minutes = minutesPicker.value
                val seconds = secondsPicker.value
                listener.onDurationSet(hours, minutes, seconds)
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    fun setOnDurationSetListener(listener: OnDurationSetListener) {
        this.listener = listener
    }

    companion object {
        fun newInstance(): DurationPickerDialogFragment {
            return DurationPickerDialogFragment()
        }
    }

}
