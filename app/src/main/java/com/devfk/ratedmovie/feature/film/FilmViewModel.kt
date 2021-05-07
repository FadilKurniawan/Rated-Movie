package com.devfk.ratedmovie.feature.film

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devfk.ratedmovie.data.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmViewModel
@Inject
constructor (private  val repo: FilmRepo):ViewModel(){

    private  val  _response = MutableLiveData<List<Movie>>()
    val responseFilm:LiveData<List<Movie>>
        get() = _response

    init {
        getAllUpcomingMovie()
    }

    private fun getAllUpcomingMovie()  = viewModelScope.launch {
        repo.getUpcomingMovie().let { response->
            if(response.isSuccessful){
                _response.postValue(response.body()?.movieList)
            }else{
                Log.d("tag","getUpcoming Movie Error : ${response.code()}")
            }
        }
    }
}