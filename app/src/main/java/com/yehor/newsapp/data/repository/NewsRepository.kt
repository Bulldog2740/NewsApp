package com.yehor.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.filter
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.data.paging.ByDateDescendingPagingSource
import com.yehor.newsapp.data.paging.ByDateNewsPagingSource
import com.yehor.newsapp.data.paging.PopularNewsPagingSource
import com.yehor.newsapp.util.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class NewsRepository @Inject constructor(private val pagingSource: PopularNewsPagingSource,
                                         private val sourceByDate: ByDateNewsPagingSource,
                                         private val sourceOldDate: ByDateDescendingPagingSource
                                         ) {

    fun getBreakingNews(): Flow<PagingData<Article>> {
        return createPager(pagingSource)
    }

    fun getByDateNews(): Flow<PagingData<Article>> {
        return createPager(sourceByDate)
    }

    fun getByOldDateNews(): Flow<PagingData<Article>> {
        return createPager(sourceOldDate)
    }

    private fun createPager(source: PagingSource<Int, Article>): Flow<PagingData<Article>> {
        return Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            )
        ) {
            source
        }.flow.map { pagingData ->
            pagingData.filter { article ->
                article.url != null && article.url.contains("https", true)
            }
        }
    }

}