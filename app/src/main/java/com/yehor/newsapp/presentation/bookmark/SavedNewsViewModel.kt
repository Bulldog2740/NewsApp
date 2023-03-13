package com.yehor.newsapp.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.data.repository.SavedNewsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SavedNewsViewModel @Inject constructor(
    val repository: SavedNewsRepository
) : ViewModel() {

    val getSavedNewsCaching = repository
        .getSavedNews()
        .cachedIn(viewModelScope)

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }

    fun invalidateSavedNewPagingSource() {
        repository.invalidate()
    }
}