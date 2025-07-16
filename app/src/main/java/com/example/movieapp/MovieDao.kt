package com.example.movieapp


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

//    @Query("SELECT * FROM movies")
//    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE Title = :title COLLATE NOCASE LIMIT 1")
    suspend fun getMovieByTitle(title: String): Movie?

    @Query("SELECT * FROM movies WHERE Actors LIKE :actorName")
    suspend fun getMoviesByActor(actorName: String): List<Movie>

}