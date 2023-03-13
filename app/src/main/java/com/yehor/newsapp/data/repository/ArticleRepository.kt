package com.yehor.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yehor.newsapp.core.InvalidatingPagingSourceFactory
import com.yehor.newsapp.data.db.ArticleDao
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.util.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val articleDao: ArticleDao) {

    suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
    }

    suspend fun deleteArticle(article: Article) {
        articleDao.deleteArticle(article)
    }

    private val pagingSource = { articleDao.getAllArticles() }
    fun getSavedNews(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),

            ) {
            pagingSource()
        }.flow
    }

}