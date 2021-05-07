package com.devfk.ratedmovie.feature.movies.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.repository.NetworkState
import com.devfk.ratedmovie.data.models.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsNetworkDataSource(private val apiService: APIService, private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState:LiveData<NetworkState> get() = _networkState

    private val _downloadedMovieDetailResponse = MutableLiveData<Movie>()
    val downloadedMovieDetailResponse:LiveData<Movie> get() = _downloadedMovieDetailResponse

    fun fetchMovieDetail(movieId:Int){
        _networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },{
                            _networkState.postValue(NetworkState.ERROR)
                            it.message?.let { it1 -> Log.e("MovieDetailDataSource", it1) }
                        }
                    )

            )
        }catch (e:Exception){
            e.message?.let { Log.e("MovieDetailDataSourceEx", it) }
        }
    }
}