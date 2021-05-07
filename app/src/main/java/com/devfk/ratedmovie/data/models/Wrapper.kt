package com.devfk.ratedmovie.data.models


import com.google.gson.annotations.SerializedName

data class Wrapper(
    val page: Int,
    @SerializedName("results")
    val movieList: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)