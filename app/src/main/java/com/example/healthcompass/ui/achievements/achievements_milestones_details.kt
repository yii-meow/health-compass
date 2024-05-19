package com.example.healthcompass.ui.achievements

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.healthcompass.R
import java.io.File
import java.io.FileOutputStream

class achievements_milestones_details : Fragment() {
    private val args by navArgs<achievements_milestones_detailsArgs>()
    private lateinit var btnDownload: Button
    private lateinit var btnShare: Button

    companion object {
        const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_achievements_milestones_details, container, false)

        val tvBadgeTitle: TextView = view.findViewById(R.id.tvBadgeTitle)
        val tvBadgeDesc: TextView = view.findViewById(R.id.tvBadgeDesc)

        tvBadgeTitle.text = args.title
        tvBadgeDesc.text = args.desc

        val badge: FrameLayout = view.findViewById(R.id.flBadge)

        btnShare = view.findViewById(R.id.btnShare)

        btnShare.setOnClickListener {
            val bitmap = captureFrameLayout(badge)
            shareBitmap(bitmap, requireContext())
        }

        btnDownload = view.findViewById(R.id.btnDownload)

        btnDownload.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val bitmap = captureFrameLayout(badge)
                saveBitmapToFile(bitmap, requireContext())
            } else {
                requestStoragePermissions()
            }
        }

        checkPermissions()

        return view
    }

    private fun captureFrameLayout(frameLayout: FrameLayout): Bitmap {
        frameLayout.isDrawingCacheEnabled = true
        frameLayout.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(frameLayout.drawingCache)
        frameLayout.isDrawingCacheEnabled = false
        return bitmap
    }

    private fun shareBitmap(bitmap: Bitmap, context: Context) {
        // Create a temporary file in the cache directory
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "shared_image.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Share the file using a FileProvider
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share image via"))
    }

    private fun saveBitmapToFile(bitmap: Bitmap, context: Context) {
        val picturesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!picturesDir.exists()) {
            picturesDir.mkdirs()
        }

        val file = File(picturesDir, "${args.title}_${args.desc}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Notify media scanner to make the file available
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            )
        )

        Toast.makeText(requireContext(), "File saved to ${file.absolutePath}", Toast.LENGTH_LONG)
            .show()
    }

    private fun requestStoragePermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_WRITE_EXTERNAL_STORAGE
        )
    }

    private fun checkPermissions() {
        btnDownload.isEnabled = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                btnDownload.isEnabled = true
                btnDownload.performClick() // Simulate button click to continue the process
            } else {
                btnDownload.isEnabled = false
            }
        }
    }
}
