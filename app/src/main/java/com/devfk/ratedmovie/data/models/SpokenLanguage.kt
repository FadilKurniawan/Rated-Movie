package com.devfk.ratedmovie.data.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "spoken_language")
data class SpokenLanguage(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("english_name")
    val englishName: String,

    @SerializedName("iso_639_1")
    val iso6391: String,
    val name: String
)