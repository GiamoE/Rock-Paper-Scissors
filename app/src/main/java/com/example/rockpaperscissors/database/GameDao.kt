package com.example.rockpaperscissors.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.rockpaperscissors.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM gameTable")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Query("DELETE FROM gameTable")
    suspend fun deleteAllGames()

    // The following queries are to display the total amount of stats.
    // We make use of count to get an INT of how many times result 'X' was present.

    @Query("SELECT COUNT(result) FROM gameTable WHERE result = 1")
    suspend fun getNumberOfWins(): Int

    @Query("SELECT COUNT(result) FROM gameTable WHERE result = 0")
    suspend fun getNumberOfDraws(): Int

    @Query("SELECT COUNT(result) FROM gameTable WHERE result = -1")
    suspend fun getNumberOfLosses(): Int
}