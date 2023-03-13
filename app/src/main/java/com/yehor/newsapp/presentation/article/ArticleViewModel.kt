package com.yehor.newsapp.presentation.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.yehor.newsapp.core.InvalidatingPagingSourceFactory
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.data.repository.ArticleRepository
import com.yehor.newsapp.data.repository.SavedNewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.text.FieldPosition
import javax.inject.Inject

class ArticleViewModel @Inject constructor(val repository: ArticleRepository) :
    ViewModel() {

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            repository.insertArticle(article)
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            repository.deleteArticle(article)
        }
    }

    private val getSavedNewsCaching = repository
        .getSavedNews()
        .cachedIn(viewModelScope)

    private val savedNewsList = mutableListOf<Article>()
     fun listSaved(): List<Article> {
        viewModelScope.launch {
            getSavedNewsCaching.collect { pagingData ->
                pagingData.map {
                    savedNewsList.add(it)
                }

            }
        }
        return savedNewsList
    }
}