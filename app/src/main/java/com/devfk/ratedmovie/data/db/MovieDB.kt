package com.devfk.ratedmovie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devfk.ratedmovie.data.models.Genre
import com.devfk.ratedmovie.data.models.Movie
import com.devfk.ratedmovie.data.models.ProductionCountry
import com.devfk.ratedmovie.data.models.SpokenLanguage

@Database(entities = [Movie::class,SpokenLanguage::class,Genre::class,ProductionCountry::class], version = 1, exportSchema = false)
abstract class MovieDB : RoomDatabase(){
    abstract fun movieDao():MovieDao
}