package com.example.lab9_e_commerce_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ProductAdapter(
    private val onAddToCartClick: (Product) -> Unit
) : ListAdapter<Product, RecyclerView.ViewHolder>(ProductDiffCallback()) {

    private var isGridView = false

    fun setGridView(isGrid: Boolean) {
        isGridView = isGrid
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridView) VIEW_TYPE_GRID else VIEW_TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_GRID) {
            val view = inflater.inflate(R.layout.item_product_grid, parent, false)
            GridViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_product_list, parent, false)
            ListViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        if (holder is ListViewHolder) {
            holder.bind(product)
        } else if (holder is GridViewHolder) {
            holder.bind(product)
        }
    }

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.productName)
        private val category: TextView = view.findViewById(R.id.productCategory)
        private val price: TextView = view.findViewById(R.id.productPrice)
        private val rating: RatingBar = view.findViewById(R.id.productRating)
        private val image: ImageView = view.findViewById(R.id.productImage)
        private val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)

        fun bind(product: Product) {
            name.text = product.name
            category.text = product.category
            price.text = String.format(Locale.getDefault(), "$%.2f", product.price)
            rating.rating = product.rating
            image.setImageResource(product.imageRes)
            btnAddToCart.text = if (product.inCart) "In Cart" else "Add to Cart"
            btnAddToCart.setOnClickListener { onAddToCartClick(product) }
        }
    }

    inner class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.productName)
        private val price: TextView = view.findViewById(R.id.productPrice)
        private val image: ImageView = view.findViewById(R.id.productImage)
        private val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)

        fun bind(product: Product) {
            name.text = product.name
            price.text = String.format(Locale.getDefault(), "$%.2f", product.price)
            image.setImageResource(product.imageRes)
            btnAddToCart.text = if (product.inCart) "In Cart" else "Add"
            btnAddToCart.setOnClickListener { onAddToCartClick(product) }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val VIEW_TYPE_LIST = 0
        private const val VIEW_TYPE_GRID = 1
    }
}
