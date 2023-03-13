package com.yehor.newsapp.presentation.breaking


import android.os.Bundle
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
import com.yehor.newsapp.R
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.databinding.FragmentBreakingNewsBinding
import com.yehor.newsapp.presentation.adapter.LoadStateAdapter
import com.yehor.newsapp.presentation.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : Fragment() {

    @Inject
    lateinit var viewModel: BreakingNewsViewModel
    private lateinit var pagingAdapter: NewsAdapter

    private val binding: FragmentBreakingNewsBinding by lazy {
        FragmentBreakingNewsBinding.inflate(layoutInflater)
    }

    private val recyclerView: RecyclerView
        get() = binding.rvBreakingNews

    private val listSpinner: ArrayList<String> by lazy {
        arrayListOf(
            getString(R.string.popular),
            getString(R.string.everything_by_new),
            getString(R.string.everything_by_old)
        )
    }

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
        viewModel.positionLiveDate.observe(viewLifecycleOwner) {
            binding.rvBreakingNews.scrollY = it
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onArticleClicked(binding.rvBreakingNews.scrollY)
    }

    private fun initAdapters() {
        spinnerAdapter()

        pagingAdapter = NewsAdapter { article ->
            val action = NewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
            findNavController().navigate(action)
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
        viewModel.onArticleClicked(binding.rvBreakingNews.scrollY)
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