package com.example.healthcompass.ui.achievements

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R
import java.io.File
import java.io.FileOutputStream

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

        val badge : FrameLayout = view.findViewById(R.id.flBadge)

        val btnShare : Button = view.findViewById(R.id.btnShare)

        btnShare.setOnClickListener{

        }

        return view
    }

    fun captureFrameLayout(frameLayout: FrameLayout): Bitmap {
        frameLayout.isDrawingCacheEnabled = true
        frameLayout.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(frameLayout.drawingCache)
        frameLayout.isDrawingCacheEnabled = false
        return bitmap
    }

    fun saveBitmapToFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captured_frame.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file
    }
}