package com.yehor.newsapp.presentation.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yehor.newsapp.databinding.FragmentSavedNewsBinding
import com.yehor.newsapp.presentation.adapter.LoadStateAdapter
import com.yehor.newsapp.presentation.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SavedNewsFragment : Fragment() {

    @Inject
    lateinit var viewModel: SavedNewsViewModel
    private lateinit var pagingAdapter: NewsAdapter

    private val binding: FragmentSavedNewsBinding by lazy {
        FragmentSavedNewsBinding.inflate(layoutInflater)
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
        viewModel.positionLiveDate.observe(viewLifecycleOwner) {
            binding.rvSavedNews.scrollY = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onArticleClicked(binding.rvSavedNews.scrollY)
    }

    private fun initAdapter() {
        pagingAdapter = NewsAdapter { article ->
            val action =
                SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
            findNavController().navigate(action)
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
                    pagingAdapter.notifyItemRemoved(position)
                    pagingAdapter.notifyDataSetChanged()
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