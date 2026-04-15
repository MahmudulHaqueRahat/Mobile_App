package com.example.lab6_news_article_reader_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle window insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        val nestedScrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)
        val btnShare = findViewById<ImageButton>(R.id.btnShare)
        val btnBackToTop = findViewById<FloatingActionButton>(R.id.btnBackToTop)
        val articleTitle = findViewById<TextView>(R.id.articleTitle).text.toString()

        // Section Views for Scrolling
        val sectionIntro = findViewById<TextView>(R.id.sectionIntro)
        val sectionKeyPoints = findViewById<TextView>(R.id.sectionKeyPoints)
        val sectionAnalysis = findViewById<TextView>(R.id.sectionAnalysis)
        val sectionConclusion = findViewById<TextView>(R.id.sectionConclusion)

        // Nav Buttons
        findViewById<Button>(R.id.navIntro).setOnClickListener {
            nestedScrollView.smoothScrollTo(0, sectionIntro.top)
        }
        findViewById<Button>(R.id.navKeyPoints).setOnClickListener {
            nestedScrollView.smoothScrollTo(0, sectionKeyPoints.top)
        }
        findViewById<Button>(R.id.navAnalysis).setOnClickListener {
            nestedScrollView.smoothScrollTo(0, sectionAnalysis.top)
        }
        findViewById<Button>(R.id.navConclusion).setOnClickListener {
            nestedScrollView.smoothScrollTo(0, sectionConclusion.top)
        }

        // Bookmark Toggle
        btnBookmark.setOnClickListener {
            isBookmarked = !isBookmarked
            if (isBookmarked) {
                btnBookmark.setImageResource(R.drawable.ic_bookmark_filled)
                Toast.makeText(this, "Article Bookmarked", Toast.LENGTH_SHORT).show()
            } else {
                btnBookmark.setImageResource(R.drawable.ic_bookmark_border)
                Toast.makeText(this, "Bookmark Removed", Toast.LENGTH_SHORT).show()
            }
        }

        // Share Intent
        btnShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, articleTitle)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share article via"))
        }

        // Back to Top
        btnBackToTop.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, 0)
        }
    }
}