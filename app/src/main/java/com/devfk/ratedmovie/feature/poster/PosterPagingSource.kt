package com.devfk.ratedmovie.feature.poster

import android.util.Log
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.data.models.Wrapper
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
class PosterPagingSource(private val apiService: APIService, private val type:TYPE, private val id:Int=0,
                         private val language:String=""):PagingSource<Int, Movie>() {

    enum class TYPE{
        POPULAR, UPCOMING, NEW, NOW_PLAY, TOP_RATED, SIMILAR
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val currentPage = params.key ?:1
        return try {
            val response: Response<Wrapper> = when (type) {
                TYPE.POPULAR    -> apiService.getPopularMovie(currentPage)
                TYPE.UPCOMING   -> apiService.getUpcomingMovie(currentPage)
                TYPE.NEW        -> apiService.getNewMovie(currentPage)
                TYPE.NOW_PLAY   -> apiService.getNowPlayMovie(currentPage)
                TYPE.TOP_RATED  -> apiService.getTopRatedMovie(currentPage)
                TYPE.SIMILAR    -> apiService.getSimilarMovies(id = id, page = currentPage, language = language)
            }

            val data = response.body()?.movieList!!
            val responseData = mutableListOf<Movie>()
            responseData.addAll(data)
            if(response.body()?.totalResults==0){
                throw IOException(Throwable("empty"))
            }
            LoadResult.Page(
                data = responseData,
                prevKey = if(currentPage==1) null else -1,
                nextKey = if(currentPage< response.body()?.totalPages!!)currentPage.plus(1) else response.body()?.totalPages
            )
        }catch (e: IOException){
            if(e.message?.contains("empty")!!){
                e.message?.let { Log.d("ERROR", it, e.cause) }
//                LoadResult.Page(
//                    data = emptyList(),
//                    prevKey = if(currentPage ==1) null else -1,
//                    nextKey = currentPage.plus(1)
//                )
            }
            LoadResult.Error(e)
        }catch (e: Exception){
            e.message?.let { Log.d("ERROR", it, e.cause) }
            LoadResult.Error(e)
        }
    }
}