package com.shuja1497.wikitap.views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.shuja1497.wikitap.R
import com.shuja1497.wikitap.utilities.INTENT_EXTRAS_URL
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val url = intent.extras?.getString(INTENT_EXTRAS_URL)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webViewProgressBar.visibility = View.GONE
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                webViewProgressBar.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }
        }

        webView.loadUrl(url)
    }
}
