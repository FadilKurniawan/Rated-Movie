package com.devfk.ratedmovie.movies

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieRepo ( private val apiService: APIService, private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, Movie>() {
    var movieLiveDataNetwork = MutableLiveData<MovieNetwork>()

    override fun create(): DataSource<Int, Movie> {
        val movieNetwork = MovieNetwork(apiService,compositeDisposable)
        movieLiveDataNetwork.postValue(movieNetwork)

        return movieNetwork
    }
}