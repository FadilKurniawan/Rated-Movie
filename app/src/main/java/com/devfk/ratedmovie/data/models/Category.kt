package com.devfk.ratedmovie.data.models


import com.google.gson.annotations.SerializedName

data class Category(
    val id: Int,
    val icon: Int,
    val title: String,
    val background: Int
)