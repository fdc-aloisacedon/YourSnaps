package com.example.yoursnaps.presentation.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yoursnaps.R
import com.example.yoursnaps.databinding.FragmentGalleryBinding
import com.example.yoursnaps.domain.GalleryDataClass
import java.io.File

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: GalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val sampleItems = listOf(
//            GalleryDataClass(R.drawable.ic_launcher_foreground, "Image 1"),
//            GalleryDataClass(R.drawable.ic_launcher_foreground, "Image 2"),
//            GalleryDataClass(R.drawable.ic_launcher_foreground, "Image 3"),
//        )

        adapter = GalleryAdapter(loadImagesFromInternalStorage())

        binding.rvGallery.layoutManager =
            GridLayoutManager(requireContext(), 2) // 2 columns
        binding.rvGallery.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadImagesFromInternalStorage(): List<GalleryDataClass> {
        val filesDir = requireContext().filesDir
        val imageFiles = (filesDir.listFiles { file -> file.extension == "jpg" || file.extension == "png" }
            ?.toList()
            ?: emptyList())

        return imageFiles.map { file ->
            GalleryDataClass(file.absolutePath, file.name)
        }
    }
}