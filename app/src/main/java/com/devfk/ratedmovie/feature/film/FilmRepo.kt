package com.devfk.ratedmovie.feature.film

import com.devfk.ratedmovie.data.api.APIService
import javax.inject.Inject

class FilmRepo
@Inject
constructor(private val apiService: APIService){

    suspend fun  getUpcomingMovie() = apiService.getUpcomingMovie()
}