package com.devfk.ratedmovie.feature.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.data.models.SpokenLanguage
import com.devfk.ratedmovie.feature.poster.PosterPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val apiService: APIService, private val repo: MovieDetailRepo) :ViewModel(){
    private val resp = MutableLiveData<Movie>()

    fun similarListMovie(id:Int, language: String) = Pager(PagingConfig(pageSize = 1)){
        PosterPagingSource(apiService, PosterPagingSource.TYPE.SIMILAR, id, language)
    }.flow.cachedIn(viewModelScope)

    val similarListMovies = Pager(PagingConfig(pageSize = 1)){
        PosterPagingSource(apiService, PosterPagingSource.TYPE.SIMILAR)
    }.flow.cachedIn(viewModelScope)
    fun movieResp(id: Int):LiveData<Movie>{
        getMovie(id)
        return resp
    }

    private fun getMovie(id:Int) = viewModelScope.launch {
        repo.getMovieDetail(id).let {response ->
            if(response.isSuccessful){
                resp.postValue(response.body())
            }else{
                Log.d("Tag", "get Movie Error Response: ${response.message()}")
            }
        }
    }
}