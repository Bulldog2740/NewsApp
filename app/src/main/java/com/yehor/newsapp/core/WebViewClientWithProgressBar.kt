package com.yehor.newsapp.core

import android.graphics.Bitmap
import android.view.View.*
import android.webkit.WebView
import android.webkit.WebViewClient
import com.yehor.newsapp.databinding.FragmentArticleBinding

class WebViewClientWithProgressBar(val binding: FragmentArticleBinding) :
        WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            binding.webViewProgressBar.visibility = VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            binding.webViewProgressBar.visibility = GONE
        }
    }