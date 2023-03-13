package com.yehor.newsapp.presentation.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.data.repository.ArticleRepository
import kotlinx.coroutines.launch
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
}