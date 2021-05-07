package com.devfk.ratedmovie.feature.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.repository.NetworkState
import com.devfk.ratedmovie.data.models.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepo (private val apiService: APIService) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesRepo: MovieRepo

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable):LiveData<PagedList<Movie>>{
        moviesRepo = MovieRepo(apiService,compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()
        moviePagedList = LivePagedListBuilder(moviesRepo, config).build()

        return moviePagedList
    }

    fun getNetworkState():LiveData<NetworkState>{
        return Transformations.switchMap(moviesRepo.movieLiveDataNetwork, MovieNetwork::networkState)
    }
}