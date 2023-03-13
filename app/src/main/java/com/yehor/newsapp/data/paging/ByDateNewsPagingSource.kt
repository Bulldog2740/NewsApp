package com.yehor.newsapp.data.paging
import com.yehor.newsapp.core.BasePagingSource
import com.yehor.newsapp.data.api.NewsService
import com.yehor.newsapp.data.model.Article
import javax.inject.Inject

class ByDateNewsPagingSource @Inject constructor(private val backend: NewsService) :
    BasePagingSource<Article>() {

    override suspend fun loadFromNetwork(params: LoadParams<Int>): List<Article> {
        val response = backend.getByDateEverything(query = "all", sortBy = "publishedAt")
        return response.body()?.articles ?: emptyList()
    }
}
