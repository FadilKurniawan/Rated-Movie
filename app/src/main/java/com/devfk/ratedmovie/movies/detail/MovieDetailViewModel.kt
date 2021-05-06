package com.devfk.ratedmovie.movies.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.devfk.ratedmovie.data.repository.NetworkState
import com.devfk.ratedmovie.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDetailViewModel (private val movieDetailRepo: MovieDetailRepo, movieId: Int):ViewModel(){
    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<Movie> by lazy {
        movieDetailRepo.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieDetailRepo.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}