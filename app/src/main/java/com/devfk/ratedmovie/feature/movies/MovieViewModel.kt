package com.devfk.ratedmovie.feature.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.devfk.ratedmovie.data.repository.NetworkState
import com.devfk.ratedmovie.data.models.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieViewModel (private val movieRepo: MoviePagedListRepo):ViewModel(){
    private val compositeDisposable = CompositeDisposable()

    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieRepo.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepo.getNetworkState()
    }

    fun listEmpty():Boolean{
        return moviePagedList.value?.isEmpty() ?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}