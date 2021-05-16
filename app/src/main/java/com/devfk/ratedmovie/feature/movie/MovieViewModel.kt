package com.devfk.ratedmovie.feature.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.devfk.ratedmovie.data.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel
@Inject
constructor(private val movieRepo:MoviesRepo): ViewModel(){

    fun insertMovie(movie:Movie) = viewModelScope.launch {
        movieRepo.insertMovie(movie)
    }
    val getAllMovies = movieRepo.getAllMovie().asLiveData()

}