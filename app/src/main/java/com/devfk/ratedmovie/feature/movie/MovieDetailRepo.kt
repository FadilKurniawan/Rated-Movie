package com.devfk.ratedmovie.feature.movie

import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.db.MovieDao
import com.devfk.ratedmovie.data.models.Movie
import javax.inject.Inject

class MovieDetailRepo @Inject
constructor(private val apiService: APIService, private val dao: MovieDao) {
    suspend fun getMovieDetail(id:Int) = apiService.getMovieDetails(id)
    suspend fun insertMovie(movie: Movie) = dao.insertMovie(movie)
    fun isMovieSaved(id: Int) = dao.isMovieSaved(id)
    suspend fun deleteMovie(movie: Movie) = dao.deleteMovie(movie)
}