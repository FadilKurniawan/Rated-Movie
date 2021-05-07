package com.devfk.ratedmovie.feature.movies.detail

import androidx.lifecycle.LiveData
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.repository.NetworkState
import com.devfk.ratedmovie.data.models.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepo (private val apiService: APIService){
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable,movieId:Int):LiveData<Movie>{
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetail(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailResponse
    }

    fun getMovieDetailsNetworkState():LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}