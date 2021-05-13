package com.devfk.ratedmovie.feature.movie

import com.devfk.ratedmovie.data.api.APIService
import javax.inject.Inject

class MovieDetailRepo @Inject
constructor(private val apiService: APIService){
    suspend fun getMovieDetail(id:Int) = apiService.getMovieDetails(id)
}