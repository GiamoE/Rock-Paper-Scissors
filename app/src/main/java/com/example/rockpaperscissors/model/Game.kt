package com.example.rockpaperscissors.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "gameTable")
data class Game(

    @ColumnInfo(name = "date")
    var date: Date,

    @ColumnInfo(name = "humanChoice")
    var humanChoice: Int,

    @ColumnInfo(name = "pcChoice")
    var pcChoice: Int,

    @ColumnInfo(name = "result")
    var result: Result,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
) : Parcelable {

    // The three possible results are stored as ints
    enum class Result(val value: Int) {
        WIN(1),
        DRAW(0),
        LOSE(-1)
    }

    // three possible choices are stored
    // allowing the use of Choice.ROCK for example.
    enum class Choice {
        ROCK,
        PAPER,
        SCISSORS
    }
}

