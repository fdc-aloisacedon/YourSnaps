package com.example.yoursnaps.presentation

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.yoursnaps.R
import com.example.yoursnaps.databinding.FragmentWelcomeBinding
import com.example.yoursnaps.framework.PermissionManager
import com.example.yoursnaps.framework.PreferencesManager

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionManager: PermissionManager
    private lateinit var preferencesManager: PreferencesManager
    private var cameraPermissionDeniedCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWelcomeBinding.bind(view)

        permissionManager = PermissionManager(requireActivity())
        preferencesManager = PreferencesManager(requireContext())

        binding.btnContinue.setOnClickListener {
            if (preferencesManager.getLocationPermission() == null) {
                requestLocationPermissionWithRationale()
            }

            if (!permissionManager.hasPermission(Manifest.permission.CAMERA)){
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainFragmentContainer, SnapsFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leak
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        preferencesManager.saveLocationPermission(isGranted.toString())
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted ->
        when {
            !permissionManager.shouldShowRationale(Manifest.permission.CAMERA) -> {
                // Permission permanently denied
                showPermissionPermanentlyDeniedDialog()
            }
            else -> {
                cameraPermissionDeniedCount++
                if (cameraPermissionDeniedCount >= 2) {
                    showPermissionPermanentlyDeniedDialog()
                } else {
                    showPermissionDeniedDialog()
                }
            }
        }

    }

    private fun requestLocationPermissionWithRationale() {
        when {
            permissionManager.shouldShowRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showLocationPermissionRationale()
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun showLocationPermissionRationale() {
        AlertDialog.Builder(requireContext())
            .setTitle("Location Permission Required")
            .setMessage("This app needs location access to show nearby places. Your location data will only be used for this feature and won't be shared with third parties.")
            .setPositiveButton("Grant Permission") { _, _ ->
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setNegativeButton("Not Now") { dialog, _ ->
                dialog.dismiss()
                // Provide alternative functionality or graceful degradation
//                showLocationDisabledFeatures()
            }
            .show()
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Camera Permission Required")
            .setMessage("This app needs camera access to continue your snaps. Images won't be shared or downloaded to your gallery")
            .setPositiveButton("Grant Permission") { _, _ ->
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setNegativeButton("Not Now") { dialog, _ ->
                dialog.dismiss()
                // Provide alternative functionality or graceful degradation
//                showLocationDisabledFeatures()
            }
            .show()
    }

    private fun showPermissionPermanentlyDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Camera permission is required for this feature. Please enable it in app settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                permissionManager.openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}