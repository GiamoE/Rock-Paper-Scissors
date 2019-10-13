package com.example.rockpaperscissors.database

import androidx.room.TypeConverter
import com.example.rockpaperscissors.model.Game
import java.util.*

class Converters {

    // with the typeconverts we change the type of the variable / value.
    // you can give a date in the parameters which data?.time gives as a long.
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun resultToInt(result: Game.Result): Int {
        return result.value
    }

    @TypeConverter
    fun intToResult(value: Int): Game.Result? {
        return Game.Result.values().first {
            it.value == value
        }
    }
}