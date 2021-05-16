package com.devfk.ratedmovie.data.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "production_country")
data class ProductionCountry(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("iso_3166_1")
    val iso31661: String,
    val name: String
)