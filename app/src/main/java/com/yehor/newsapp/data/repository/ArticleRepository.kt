package com.yehor.newsapp.data.repository

import com.yehor.newsapp.data.db.ArticleDao
import com.yehor.newsapp.data.model.Article
import javax.inject.Inject

class ArticleRepository @Inject constructor(private val articleDao: ArticleDao) {

    suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
    }

    suspend fun deleteArticle(article: Article) {
        articleDao.deleteArticle(article)
    }
}