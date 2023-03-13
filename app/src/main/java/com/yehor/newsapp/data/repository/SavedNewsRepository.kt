package com.yehor.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yehor.newsapp.data.db.ArticleDao
import com.yehor.newsapp.data.model.Article
import com.yehor.newsapp.core.InvalidatingPagingSourceFactory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedNewsRepository @Inject constructor(private val articleDao: ArticleDao) {

    private val pagingSource = { articleDao.getAllArticles() }
    private val invalidatingPagingSourceFactory = InvalidatingPagingSourceFactory { pagingSource() }

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

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

    suspend fun deleteArticle(article: Article) {
        articleDao.deleteArticle(article)
    }

    fun invalidate() {
        pagingSource().invalidate()
        invalidatingPagingSourceFactory.invalidate()
    }
}