package com.devfk.ratedmovie.feature.banner

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.models.Movie
import java.lang.Exception

class BannerPagingSource(private val apiService: APIService):PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?:1
            val response = apiService.getUpcomingMovie(currentPage)
            val data = response.body()?.movieList?: emptyList()
            val responseData = mutableListOf<Movie>()
            responseData.addAll(data)
            LoadResult.Page(
                data = responseData,
                prevKey = if(currentPage==1) null else -1,
                nextKey = currentPage.plus(1)
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}