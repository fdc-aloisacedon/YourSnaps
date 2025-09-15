package com.example.yoursnaps.presentation.gallery

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yoursnaps.databinding.ItemGalleryBinding
import com.example.yoursnaps.domain.GalleryDataClass

class GalleryAdapter(private val items: List<GalleryDataClass>) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    class GalleryViewHolder(val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val file = items[position]

        // Load image into ImageView
        val bitmap = BitmapFactory.decodeFile(file.photo)
        holder.binding.ivImage.setImageBitmap(bitmap)

        holder.binding.tvTitle.text = file.title
    }

    override fun getItemCount() = items.size
}