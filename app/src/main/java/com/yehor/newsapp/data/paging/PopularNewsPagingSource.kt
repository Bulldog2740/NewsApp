package com.yehor.newsapp.data.paging

import com.yehor.newsapp.core.BasePagingSource
import com.yehor.newsapp.data.api.NewsService
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.util.NEWS_API_STARTING_PAGE_INDEX
import javax.inject.Inject

class PopularNewsPagingSource @Inject constructor(private val backend: NewsService) :
    BasePagingSource<Article>() {

    override suspend fun loadFromNetwork(params: LoadParams<Int>): List<Article> {
        val response = backend.getBreakingNews(pageNumber = params.key ?: NEWS_API_STARTING_PAGE_INDEX)
        return response.body()?.articles ?: emptyList()
    }
}