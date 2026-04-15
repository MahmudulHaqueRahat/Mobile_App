package com.example.lab9_e_commerce_app

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab9_e_commerce_app.databinding.ActivityCartBinding
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: ProductAdapter
    private var cartItems = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCart)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarCart.setNavigationOnClickListener { finish() }

        val items = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("CART_ITEMS", ArrayList::class.java) as? ArrayList<Product>
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("CART_ITEMS") as? ArrayList<Product>
        }

        if (items != null) {
            cartItems = items.toMutableList()
        }
        
        setupRecyclerView()
        updateTotal()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter { product ->
            product.inCart = false
            cartItems.remove(product)
            adapter.submitList(ArrayList(cartItems))
            updateTotal()
        }
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCart.adapter = adapter
        adapter.submitList(ArrayList(cartItems))
    }

    private fun updateTotal() {
        val total = cartItems.sumOf { it.price }
        binding.tvTotalPrice.text = String.format(Locale.getDefault(), "Total: $%.2f", total)
    }
}
