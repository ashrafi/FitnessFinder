package com.test.fitnessstudios.core.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import kotlinx.datetime.*
import java.util.*


class Converters {
    @TypeConverter
    fun fromTimestampLong(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: Int?): LocalDate? {
        return value?.let {
            LocalDate.fromEpochDays(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun LocalDateTimeToTimestamp(date: LocalDate?): Int? {
        return date?.toEpochDays()
    }

    @TypeConverter
    fun fromTimestrap(dateString: String?): LocalDateTime? {
        return dateString?.let { LocalDateTime.parse(dateString) }
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return date?.toString()
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun InstantToTimestamp(instance: Instant?): LocalDateTime? {
        return instance.toLocalDateTime(TimeZone.UTC)
    }*/

}