package com.yehor.newsapp.core
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yehor.newsapp.util.NEWS_API_STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

abstract class BasePagingSource<T : Any> : PagingSource<Int, T>() {

    abstract suspend fun loadFromNetwork(params: LoadParams<Int>): List<T>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val currentLoadingPageKey = params.key ?: NEWS_API_STARTING_PAGE_INDEX
            val responseData = loadFromNetwork(params)
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentLoadingPageKey == NEWS_API_STARTING_PAGE_INDEX) null else currentLoadingPageKey - 1,
                nextKey = if (responseData.isEmpty()) null else currentLoadingPageKey.plus(1)
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}