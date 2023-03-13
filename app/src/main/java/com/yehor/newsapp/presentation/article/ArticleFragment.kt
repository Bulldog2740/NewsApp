package com.yehor.newsapp.presentation.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.yehor.newsapp.R
import com.yehor.newsapp.core.WebViewClientWithProgressBar
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.databinding.FragmentArticleBinding
import com.yehor.newsapp.databinding.FragmentBreakingNewsBinding
import com.yehor.newsapp.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : Fragment() {

    @Inject
    lateinit var viewModel: ArticleViewModel
    private lateinit var article: Article
    private val args: ArticleFragmentArgs by navArgs()

    private val binding: FragmentArticleBinding by lazy {
      FragmentArticleBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setBottomBarVisibility(false)
    }

    override fun onPause() {
        super.onPause()
       (requireActivity() as MainActivity).setBottomBarVisibility(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveArticleArgsAndOpenWebView()
        setupFloatingActionButton()
    }

    private fun receiveArticleArgsAndOpenWebView() {
        article = args.article
        binding.webView.apply {
            webViewClient = WebViewClientWithProgressBar(binding)
            article.url?.let { loadUrl(it) }
            settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }
    }

    private fun setupFloatingActionButton() {
        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(requireView(), "Article saved successfully", Snackbar.LENGTH_SHORT)
                .apply {
                    setAction("Undo") {
                        viewModel.deleteArticle(article)
                    }
                    show()
                }
        }
    }
}