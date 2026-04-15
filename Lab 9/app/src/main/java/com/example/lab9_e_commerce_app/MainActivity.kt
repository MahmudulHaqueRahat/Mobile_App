package com.example.lab9_e_commerce_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab9_e_commerce_app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Collections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    private var allProducts = mutableListOf<Product>()
    private var filteredProducts = mutableListOf<Product>()
    private var isGridView = false
    private var currentCategory = "All"
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        
        setupRecyclerView() // Initialize adapter first
        setupDummyData()
        setupCategoryChips()
        setupItemTouchHelper()
    }

    private fun setupDummyData() {
        allProducts = mutableListOf(
            Product(1, "Laptop", 999.99, 4.5f, "Electronics", R.drawable.laptop),
            Product(2, "T-Shirt", 19.99, 4.0f, "Clothing", R.drawable.shirt),
            Product(3, "Kotlin Guide", 29.99, 5.0f, "Books", R.drawable.book),
            Product(4, "Burger", 5.99, 4.2f, "Food", R.drawable.burger),
            Product(5, "Action Figure", 15.00, 4.8f, "Toys", android.R.drawable.ic_menu_report_image),
            Product(6, "Smartphone", 699.00, 4.6f, "Electronics", R.drawable.iphone),
            Product(7, "Jeans", 49.99, 4.3f, "Clothing", R.drawable.shirt),
            Product(8, "Cooking Book", 24.50, 4.1f, "Books", R.drawable.book)
        )
        updateFilteredList()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter { product ->
            product.inCart = !product.inCart
            val index = filteredProducts.indexOf(product)
            if (index != -1) {
                adapter.notifyItemChanged(index)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupCategoryChips() {
        binding.categoryChips.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: R.id.chipAll
            currentCategory = when (checkedId) {
                R.id.chipElectronics -> "Electronics"
                R.id.chipClothing -> "Clothing"
                R.id.chipBooks -> "Books"
                R.id.chipFood -> "Food"
                R.id.chipToys -> "Toys"
                else -> "All"
            }
            updateFilteredList()
        }
    }

    private fun updateFilteredList() {
        filteredProducts = allProducts.filter {
            (currentCategory == "All" || it.category == currentCategory) &&
                    (it.name.contains(searchQuery, ignoreCase = true))
        }.toMutableList()

        if (::adapter.isInitialized) {
            adapter.submitList(ArrayList(filteredProducts))
        }
        binding.emptyState.visibility = if (filteredProducts.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupItemTouchHelper() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                if (fromPos != -1 && toPos != -1) {
                    Collections.swap(filteredProducts, fromPos, toPos)
                    adapter.notifyItemMoved(fromPos, toPos)
                }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != -1) {
                    val deletedProduct = filteredProducts[position]
                    val originalIndex = allProducts.indexOf(deletedProduct)

                    filteredProducts.removeAt(position)
                    allProducts.removeAt(originalIndex)
                    adapter.submitList(ArrayList(filteredProducts))

                    Snackbar.make(binding.recyclerView, "${deletedProduct.name} deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO") {
                            allProducts.add(originalIndex, deletedProduct)
                            updateFilteredList()
                        }.show()
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText ?: ""
                updateFilteredList()
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_view -> {
                isGridView = !isGridView
                item.setIcon(if (isGridView) android.R.drawable.ic_menu_view else android.R.drawable.ic_menu_sort_by_size)
                adapter.setGridView(isGridView)
                binding.recyclerView.layoutManager = if (isGridView) GridLayoutManager(this, 2) else LinearLayoutManager(this)
                adapter.notifyDataSetChanged()
                true
            }
            R.id.action_cart -> {
                val cartItems = ArrayList(allProducts.filter { it.inCart })
                val intent = Intent(this, CartActivity::class.java)
                intent.putExtra("CART_ITEMS", cartItems)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
