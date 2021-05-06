package com.devfk.ratedmovie.movies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.repository.NetworkState
import com.devfk.ratedmovie.data.vo.Movie
import com.devfk.ratedmovie.data.vo.Wrapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieNetwork (private val apiService: APIService,private val compositeDisposable: CompositeDisposable):
    PageKeyedDataSource<Int, Movie>() {

    private var page =1

    val networkState:MutableLiveData<NetworkState> get() = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getPopularMovie(page)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        callback.onResult(it.movieList, null, page+1)
                    },{ it ->
                        networkState.postValue(NetworkState.ERROR)
                        it.message?.let { Log.e("MovieListNetwork", it) }
                    })
            )
        }catch (e:Exception){
            e.message?.let { Log.e("MovieListNetwork", it) }
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        try {
            compositeDisposable.add(
                apiService.getPopularMovie(page)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        if (it.totalPages >= params.key) {
                            callback.onResult(it.movieList,params.key+1)
                        }else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    }, { it ->
                        networkState.postValue(NetworkState.ERROR)
                        it.message?.let { Log.e("MovieListNetwork", it) }
                    })
            )
        }catch (e:Exception){
            e.message?.let { Log.e("MovieListNetwork", it) }
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}