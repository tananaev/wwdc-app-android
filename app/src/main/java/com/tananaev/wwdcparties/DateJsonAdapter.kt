package com.tananaev.wwdcparties

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DateJsonAdapter : JsonAdapter<Date>() {

    companion object {
        val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"
    }

    @Synchronized @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Date {
        return SimpleDateFormat(DATE_FORMAT, Locale.US).parse(reader.nextString())
    }

    @Synchronized @Throws(IOException::class)
    override fun toJson(writer: JsonWriter?, value: Date?) {
        writer?.value(SimpleDateFormat(DATE_FORMAT, Locale.US).format(value))
    }

}
