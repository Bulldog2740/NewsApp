package com.yehor.newsapp.presentation.breaking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yehor.newsapp.data.repository.NewsRepository
import javax.inject.Inject

class BreakingNewsViewModel @Inject constructor(
    val repository: NewsRepository
) : ViewModel() {

    val breakingNewsStream = repository.getBreakingNews()
        .cachedIn(viewModelScope)

    val getByDateNews = repository.getByDateNews()
        .cachedIn(viewModelScope)

    val getByOldDateNews = repository.getByOldDateNews()
        .cachedIn(viewModelScope)

    private val _positionLiveDate = MutableLiveData<Int>()
    val positionLiveDate = liveData<Int> {
        emitSource(_positionLiveDate)
    }
    fun onArticleClicked(position: Int) {
        _positionLiveDate.postValue(position)
    }
}