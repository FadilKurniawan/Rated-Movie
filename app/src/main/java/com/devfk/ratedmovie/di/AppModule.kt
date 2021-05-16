package com.devfk.ratedmovie.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devfk.ratedmovie.data.api.APIService
import com.devfk.ratedmovie.data.api.BaseService
import com.devfk.ratedmovie.data.db.MovieDB
import com.devfk.ratedmovie.data.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constant.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofitInstance(BASE_URL:String):APIService =
        BaseService.getClient()


    @Singleton
    @Provides
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,MovieDB::class.java,
        "movie_db"
    ).build()

    @Singleton
    @Provides
    fun provideMovieDao(
        db: MovieDB
    ) = db.movieDao()

}