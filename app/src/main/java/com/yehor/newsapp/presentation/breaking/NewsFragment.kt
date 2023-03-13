package com.yehor.newsapp.presentation.breaking


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yehor.newsapp.R
import com.yehor.newsapp.core.autoCleared
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.databinding.FragmentBreakingNewsBinding
import com.yehor.newsapp.presentation.adapter.LoadStateAdapter
import com.yehor.newsapp.presentation.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NewsFragment : Fragment() {

    @Inject
    lateinit var viewModel: BreakingNewsViewModel
    private var pagingAdapter by autoCleared<NewsAdapter>()

    private val binding: FragmentBreakingNewsBinding by lazy {
        FragmentBreakingNewsBinding.inflate(layoutInflater)
    }

    private val recyclerView: RecyclerView
        get() = binding.rvBreakingNews
    private var listSpinner = arrayOf("Popular", "Everything by new", "Everything by old")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        binding.retry.setOnClickListener { pagingAdapter.retry() }
        decorateRecyclerView()
        observeNewsData(viewModel.breakingNewsStream)
    }

    private fun initAdapters() {
        spinnerAdapter()
        pagingAdapter = NewsAdapter {article ->
            kotlin.runCatching {
                val action = NewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
                findNavController().navigate(action)
            }.onFailure {
                Log.d("TAG_EVENT", "initAdapters: ${it.message}")
            }
        }
        pagingAdapter.apply {
            recyclerView.adapter = withLoadStateFooter(
                footer = LoadStateAdapter {
                    retry()
                }
            )
            // This gives us the load state for the PageSource
            // Or the load state for RemoteMediator needed for network & database case
            addLoadStateListener { loadState ->
                binding.loadState = loadState
            }
        }
    }

    private fun spinnerAdapter() {
        val sortAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listSpinner
        )
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sortSpinner.adapter = sortAdapter
        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    listSpinner[0] -> observeNewsData(viewModel.breakingNewsStream)
                    listSpinner[1] -> observeNewsData(viewModel.getByDateNews)
                    listSpinner[2] -> observeNewsData(viewModel.getByOldDateNews)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun observeNewsData(dataStream: Flow<PagingData<Article>>) {
        lifecycleScope.launch {
            dataStream.collectLatest {
                pagingAdapter.submitData(it)
            }
        }
    }

    private fun decorateRecyclerView() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
    }
}