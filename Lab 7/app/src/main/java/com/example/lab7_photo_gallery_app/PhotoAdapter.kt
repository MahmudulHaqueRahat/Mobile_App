package com.example.lab7_photo_gallery_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

class PhotoAdapter(
    private var photos: List<Photo>,
    private var isSelectionMode: Boolean = false
) : BaseAdapter() {

    fun updateData(newPhotos: List<Photo>) {
        photos = newPhotos
        notifyDataSetChanged()
    }

    fun setSelectionMode(enabled: Boolean) {
        isSelectionMode = enabled
        notifyDataSetChanged()
    }

    override fun getCount(): Int = photos.size

    override fun getItem(position: Int): Photo = photos[position]

    override fun getItemId(position: Int): Long = photos[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_photo, parent, false)

        val photo = getItem(position)
        val ivPhoto = view.findViewById<ImageView>(R.id.ivPhoto)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val cbSelect = view.findViewById<CheckBox>(R.id.cbSelect)

        ivPhoto.setImageResource(photo.resourceId)
        tvTitle.text = photo.title
        
        cbSelect.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
        cbSelect.isChecked = photo.isSelected

        cbSelect.setOnCheckedChangeListener { _, isChecked ->
            photo.isSelected = isChecked
        }

        return view
    }
}
