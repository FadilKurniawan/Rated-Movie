package com.devfk.ratedmovie.feature.movie

import com.devfk.ratedmovie.data.db.MovieDao
import com.devfk.ratedmovie.data.models.Movie
import javax.inject.Inject

class MoviesRepo
@Inject
constructor(private val dao: MovieDao){
    suspend fun insertMovie(movie: Movie) = dao.insertMovie(movie)
    fun getAllMovie() = dao.getAllMovies()
}