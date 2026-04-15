package com.example.lab8_learning_portal_app

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var etAddress: EditText
    private lateinit var progressBar: ProgressBar
    private val defaultUrl = "https://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        etAddress = findViewById(R.id.etAddress)
        progressBar = findViewById(R.id.progressBar)

        setupWebView()
        setupButtons()
        setupAddressBar()

        webView.loadUrl(defaultUrl)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    // Stop handling back press here and let the system handle it
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = true
            displayZoomControls = false
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                progressBar.progress = 0
                // Update address bar but avoid infinite loops or clearing user input while typing
                if (url != null && !etAddress.hasFocus()) {
                    etAddress.setText(url)
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                if (url != null && !etAddress.hasFocus()) {
                    etAddress.setText(url)
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                // Only load offline page for main frame errors to avoid infinite loops on subresources
                if (request?.isForMainFrame == true) {
                    webView.loadUrl("file:///android_asset/offline.html")
                }
            }
            
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }
    }

    private fun setupButtons() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            if (webView.canGoBack()) webView.goBack()
            else Toast.makeText(this, "No more history", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.btnForward).setOnClickListener {
            if (webView.canGoForward()) webView.goForward()
        }

        findViewById<ImageButton>(R.id.btnRefresh).setOnClickListener {
            webView.reload()
        }

        findViewById<ImageButton>(R.id.btnHome).setOnClickListener {
            webView.loadUrl(defaultUrl)
        }

        findViewById<Button>(R.id.btnGo).setOnClickListener {
            loadUrlFromEditText()
        }

        // Shortcuts
        findViewById<Button>(R.id.btnGoogle).setOnClickListener { webView.loadUrl("https://www.google.com") }
        findViewById<Button>(R.id.btnYouTube).setOnClickListener { webView.loadUrl("https://www.youtube.com") }
        findViewById<Button>(R.id.btnWikipedia).setOnClickListener { webView.loadUrl("https://www.wikipedia.org") }
        findViewById<Button>(R.id.btnKhanAcademy).setOnClickListener { webView.loadUrl("https://www.khanacademy.org") }
        findViewById<Button>(R.id.btnUniversity).setOnClickListener { webView.loadUrl("https://www.harvard.edu") }
    }

    private fun setupAddressBar() {
        etAddress.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                loadUrlFromEditText()
                true
            } else {
                false
            }
        }
    }

    private fun loadUrlFromEditText() {
        var url = etAddress.text.toString().trim()
        if (url.isNotEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }
            webView.loadUrl(url)
            etAddress.clearFocus() // Hide keyboard and stop updating text while loading
        }
    }
}