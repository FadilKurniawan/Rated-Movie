package com.devfk.ratedmovie.data.api

import com.devfk.ratedmovie.data.vo.Movie
import com.devfk.ratedmovie.data.vo.Wrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {


    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int): Single<Movie>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page:Int): Single<Wrapper>
}