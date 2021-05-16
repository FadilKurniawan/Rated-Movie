package com.devfk.ratedmovie.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.devfk.ratedmovie.data.models.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movie") //ORDER BY addedAt ASC")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("SELECT MAX(addedNum) FROM movie ")
    fun getCountMax(): LiveData<Int>

    @Query("SELECT * FROM movie WHERE id=:id")
    fun isMovieSaved(id: Int): Flow<List<Movie>>

    @Delete
    suspend fun deleteMovie(movie: Movie)
}