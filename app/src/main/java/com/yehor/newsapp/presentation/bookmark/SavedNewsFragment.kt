package com.yehor.newsapp.presentation.bookmark

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.yehor.newsapp.R
import com.yehor.newsapp.core.autoCleared
import com.yehor.newsapp.databinding.FragmentSavedNewsBinding
import com.yehor.newsapp.presentation.adapter.LoadStateAdapter
import com.yehor.newsapp.presentation.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    @Inject
    lateinit var  viewModel: SavedNewsViewModel
    private var pagingAdapter by autoCleared<NewsAdapter>()

    private val binding : FragmentSavedNewsBinding by viewBinding(
        FragmentSavedNewsBinding::bind,
        onViewDestroyed = { _: FragmentSavedNewsBinding ->
            // reset view
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.retry.setOnClickListener { pagingAdapter.retry() }
        decorateRecyclerView()
        subscribeUI()
    }

    private fun subscribeUI() {
        lifecycleScope.launch {
            viewModel.getSavedNewsCaching.collectLatest {
                pagingAdapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        pagingAdapter = NewsAdapter {
            kotlin.runCatching {
                val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(
                    it
                )
                findNavController().navigate(action)
            }
        }
        binding.rvSavedNews.adapter = pagingAdapter.withLoadStateFooter(
            footer = LoadStateAdapter { pagingAdapter.retry() }
        )
        pagingAdapter.addLoadStateListener { loadState -> binding.loadState = loadState }

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val article = pagingAdapter.getItemAt(position)

                if (article != null) {
                    viewModel.deleteArticle(article)
                    pagingAdapter.notifyItemRangeRemoved(position, 1)
                    viewModel.invalidateSavedNewPagingSource()
                    pagingAdapter.refresh()
                }
                Snackbar.make(requireView(), "Successfully deleted article", Snackbar.LENGTH_SHORT)
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }
    }

    private fun decorateRecyclerView() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.rvSavedNews.addItemDecoration(decoration)
    }
}