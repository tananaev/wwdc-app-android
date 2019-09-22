package com.tananaev.wwdc.parties

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter : JsonAdapter<Date>() {

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"
    }

    @TypeConverter
    fun toDate(value: String?): Date {
        return SimpleDateFormat(DATE_FORMAT, Locale.US).parse(value)
    }

    @TypeConverter
    fun fromDate(value: Date?): String {
        return SimpleDateFormat(DATE_FORMAT, Locale.US).format(value)
    }

    @Synchronized @Throws(IOException::class)
    override fun toJson(writer: JsonWriter?, value: Date?) {
        writer?.value(fromDate(value))
    }

    @Synchronized @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Date {
        return toDate(reader.nextString())
    }

}
