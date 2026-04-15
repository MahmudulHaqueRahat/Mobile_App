package com.example.lab5_contact_book_app

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

class ContactAdapter(context: Context, private val contactList: MutableList<Contact>) :
    ArrayAdapter<Contact>(context, 0, contactList), Filterable {

    // Initialize with a copy of the source list
    private var filteredList: MutableList<Contact> = ArrayList(contactList)
    private var currentQuery: CharSequence? = null

    override fun getCount(): Int = filteredList.size
    override fun getItem(position: Int): Contact? = if (position in filteredList.indices) filteredList[position] else null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        val contact = getItem(position)

        val tvAvatar = view.findViewById<TextView>(R.id.tvAvatar)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)

        contact?.let {
            tvName.text = it.name
            tvPhone.text = it.phone
            tvAvatar.text = it.initial

            val colors = listOf("#3F51B5", "#4CAF50", "#FF9800", "#F44336")
            val color = Color.parseColor(colors[it.name.length % colors.size])
            tvAvatar.background?.setTint(color)
        }
        return view
    }

    // This ensures that when MainActivity calls notifyDataSetChanged(), 
    // the filteredList is updated from the source contactList.
    override fun notifyDataSetChanged() {
        if (currentQuery.isNullOrEmpty()) {
            filteredList = ArrayList(contactList)
        } else {
            // Re-apply the filter to include new items if they match the query
            filter.filter(currentQuery)
        }
        super.notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                currentQuery = constraint
                val charSearch = constraint?.toString() ?: ""
                val resultsList = if (charSearch.isEmpty()) {
                    contactList
                } else {
                    contactList.filter { it.name.contains(charSearch, ignoreCase = true) }
                }
                
                val filterResults = FilterResults()
                filterResults.values = resultsList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = (results?.values as? List<Contact>)?.toMutableList() ?: mutableListOf()
                // Use the super method directly to avoid re-triggering the sync logic
                superNotifyDataSetChanged()
            }
        }
    }

    private fun superNotifyDataSetChanged() {
        super.notifyDataSetChanged()
    }
}
