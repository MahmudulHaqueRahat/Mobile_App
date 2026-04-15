package com.example.lab7_photo_gallery_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var adapter: PhotoAdapter
    private lateinit var selectionToolbar: Toolbar
    private lateinit var chipGroup: ChipGroup
    private lateinit var fabAdd: FloatingActionButton

    private var allPhotos = mutableListOf<Photo>()
    private var displayedPhotos = mutableListOf<Photo>()
    private var isSelectionMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initViews()
        setupListeners()
    }

    private fun initData() {
        val categories = listOf("Nature", "City", "Animals", "Food", "Travel")
        val drawables = listOf(
            R.drawable.cat, R.drawable.fox, R.drawable.bird, 
            R.drawable.hippo, R.drawable.panda
        )

        // Generating 15 photos using the available drawables
        for (i in 1..15) {
            val resId = drawables[i % drawables.size]
            val category = categories[i % categories.size]
            allPhotos.add(Photo(i.toLong(), resId, "Photo $i", category))
        }
        displayedPhotos.addAll(allPhotos)
    }

    private fun initViews() {
        gridView = findViewById(R.id.gridView)
        selectionToolbar = findViewById(R.id.selectionToolbar)
        chipGroup = findViewById(R.id.chipGroup)
        fabAdd = findViewById(R.id.fabAdd)

        adapter = PhotoAdapter(displayedPhotos)
        gridView.adapter = adapter
    }

    private fun setupListeners() {
        gridView.setOnItemClickListener { _, _, position, _ ->
            val photo = displayedPhotos[position]
            if (isSelectionMode) {
                photo.isSelected = !photo.isSelected
                updateSelectionCount()
                adapter.notifyDataSetChanged()
            } else {
                val intent = Intent(this, FullscreenActivity::class.java)
                intent.putExtra("image_res_id", photo.resourceId)
                startActivity(intent)
            }
        }

        gridView.setOnItemLongClickListener { _, _, position, _ ->
            if (!isSelectionMode) {
                isSelectionMode = true
                displayedPhotos[position].isSelected = true
                adapter.setSelectionMode(true)
                selectionToolbar.visibility = View.VISIBLE
                updateSelectionCount()
            }
            true
        }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedId = if (checkedIds.isNotEmpty()) checkedIds[0] else View.NO_ID
            filterPhotos(checkedId)
        }

        findViewById<ImageButton>(R.id.btnCloseSelection).setOnClickListener {
            exitSelectionMode()
        }

        findViewById<ImageButton>(R.id.btnDelete).setOnClickListener {
            deleteSelectedPhotos()
        }

        findViewById<ImageButton>(R.id.btnShare).setOnClickListener {
            val selectedCount = allPhotos.count { it.isSelected }
            if (selectedCount > 0) {
                Toast.makeText(this, "Sharing $selectedCount photos", Toast.LENGTH_SHORT).show()
                exitSelectionMode()
            } else {
                Toast.makeText(this, "No photos selected", Toast.LENGTH_SHORT).show()
            }
        }

        fabAdd.setOnClickListener {
            addRandomPhoto()
        }
    }

    private fun filterPhotos(checkedId: Int) {
        val category = when (checkedId) {
            R.id.chipNature -> "Nature"
            R.id.chipCity -> "City"
            R.id.chipAnimals -> "Animals"
            R.id.chipFood -> "Food"
            R.id.chipTravel -> "Travel"
            else -> "All"
        }

        displayedPhotos.clear()
        if (category == "All") {
            displayedPhotos.addAll(allPhotos)
        } else {
            displayedPhotos.addAll(allPhotos.filter { it.category == category })
        }
        adapter.updateData(displayedPhotos)
    }

    private fun updateSelectionCount() {
        val count = allPhotos.count { it.isSelected }
        selectionToolbar.title = "$count selected"
    }

    private fun exitSelectionMode() {
        isSelectionMode = false
        allPhotos.forEach { it.isSelected = false }
        adapter.setSelectionMode(false)
        selectionToolbar.visibility = View.GONE
    }

    private fun deleteSelectedPhotos() {
        val selectedPhotos = allPhotos.filter { it.isSelected }
        val count = selectedPhotos.size
        if (count > 0) {
            allPhotos.removeAll(selectedPhotos)
            filterPhotos(chipGroup.checkedChipId)
            Toast.makeText(this, "$count photos deleted", Toast.LENGTH_SHORT).show()
            exitSelectionMode()
        } else {
            Toast.makeText(this, "No photos selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addRandomPhoto() {
        val categories = listOf("Nature", "City", "Animals", "Food", "Travel")
        val drawables = listOf(
            R.drawable.cat, R.drawable.fox, R.drawable.bird, 
            R.drawable.hippo, R.drawable.panda
        )
        
        val newId = (allPhotos.maxOfOrNull { it.id } ?: 0) + 1
        val randomPhoto = Photo(
            newId,
            drawables.random(),
            "New Photo $newId",
            categories.random()
        )
        allPhotos.add(randomPhoto)
        filterPhotos(chipGroup.checkedChipId)
        Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show()
    }
}
