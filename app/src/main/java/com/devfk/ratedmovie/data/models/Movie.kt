package com.devfk.ratedmovie.data.models


import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class Movie(
    var adult: Boolean? = false,
    @SerializedName("backdrop_path")
    var backdropPath: String? = "",
    @SerializedName("belongs_to_collection")
    @Embedded var belongsToCollection: Any? = false,
    var budget: Int? = 0,
    @Ignore  var genres: List<Genre>? = listOf(),
    var homepage: String? = "",
    @PrimaryKey
    var id: Int? = 0,
    @SerializedName("imdb_id")
    var imdbId: String? = "",
    @SerializedName("original_language")
    var originalLanguage: String? = "",
    @SerializedName("original_title")
    var originalTitle: String? = "",
    var overview: String? = "",
    var popularity: Double? = 0.0,
    @SerializedName("poster_path")
    var posterPath: String? = "",
    @SerializedName("production_countries")
    @Ignore  var productionCountries: List<ProductionCountry>? = listOf(),
    @SerializedName("release_date")
    var releaseDate: String? = "",
    var revenue: Long? = 0L,
    var runtime: Int? = 0,
    @SerializedName("spoken_languages")
    @Ignore var spokenLanguages: List<SpokenLanguage>? = null,
    var status: String? = "",
    var tagline: String? = "",
    var title: String? = "",
    var video: Boolean? = false,
    var addedNum: Int? = 0,
    @SerializedName("vote_average")
    var voteAverage: Double? = 0.0,
    @SerializedName("vote_count")
    var voteCount: Int? = 0
)
//{
//    constructor():this(false? = null,
//        ""? = null,
//        ""? = null,
//        0? = null,
//        listOf()? = null,
//        ""? = null,
//        0? = null,
//        ""? = null,
//        ""? = null,
//        ""? = null,
//        ""? = null,
//        0.0? = null,
//        ""? = null,
//        listOf()? = null,
//        ""? = null,
//        0L? = null,
//        0? = null,
//        listOf()? = null,
//        ""? = null,
//        ""? = null,
//        ""? = null,
//        false? = null,
//        0? = null,
//        0.0? = null,
//        0
//
//
//    )
//}