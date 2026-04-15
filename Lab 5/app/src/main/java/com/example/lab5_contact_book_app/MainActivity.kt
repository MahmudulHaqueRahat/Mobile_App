package com.example.lab5_contact_book_app

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val contacts = mutableListOf<Contact>()
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.listView)
        val searchView = findViewById<SearchView>(R.id.searchView)
        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)

        adapter = ContactAdapter(this, contacts)
        listView.adapter = adapter
        listView.emptyView = findViewById(android.R.id.empty)

        // 1. ADD CONTACT (FAB)
        fab.setOnClickListener { showAddContactDialog() }

        // 2. VIEW DETAILS (Short Click)
        listView.setOnItemClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position)
            Toast.makeText(this, "Name: ${contact?.name}\nEmail: ${contact?.email}", Toast.LENGTH_LONG).show()
        }

        // 3. DELETE (Long Click)
        listView.setOnItemLongClickListener { _, _, position, _ ->
            val contact = adapter.getItem(position)
            AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete ${contact?.name}?")
                .setPositiveButton("Yes") { _, _ ->
                    contacts.remove(contact)
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton("No", null)
                .show()
            true
        }

        // 4. SEARCH LOGIC
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun showAddContactDialog() {
        val nameEt = EditText(this).apply { hint = "Name" }
        val phoneEt = EditText(this).apply { hint = "Phone"; inputType = InputType.TYPE_CLASS_PHONE }
        val emailEt = EditText(this).apply { hint = "Email" }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
            addView(nameEt); addView(phoneEt); addView(emailEt)
        }

        AlertDialog.Builder(this)
            .setTitle("Add New Contact")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val name = nameEt.text.toString()
                val phone = phoneEt.text.toString()
                val email = emailEt.text.toString()
                if (name.isNotEmpty()) {
                    contacts.add(Contact(name, phone, email))
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
