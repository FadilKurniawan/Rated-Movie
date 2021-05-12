package com.devfk.ratedmovie.feature.poster

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.data.models.Wrapper
import retrofit2.Response
import java.lang.Exception
class PosterPagingSource(private val apiService: APIService, private val type:TYPE):PagingSource<Int, Movie>() {

    enum class TYPE{
        POPULAR, UPCOMING, NEW, NOW_PLAY, TOP_RATED
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?:1
            val response: Response<Wrapper> = when (type) {
                TYPE.POPULAR    -> apiService.getPopularMovie(currentPage)
                TYPE.UPCOMING   -> apiService.getUpcomingMovie(currentPage)
                TYPE.NEW        -> apiService.getNewMovie(currentPage)
                TYPE.NOW_PLAY   -> apiService.getNowPlayMovie(currentPage)
                TYPE.TOP_RATED  -> apiService.getTopRatedMovie(currentPage)
            }

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