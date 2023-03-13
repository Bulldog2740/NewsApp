package com.yehor.newsapp.data.api

import com.yehor.newsapp.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int = 1
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getByDateEverything(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String
    ): Response<NewsResponse>
}