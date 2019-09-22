package com.tananaev.wwdc.parties

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Party(
        @PrimaryKey val objectId: String,
        val icon: String,
        val logo: String,
        val title: String,
        val startDate: Date,
        val endDate: Date,
        val details: String,
        val address1: String,
        val address2: String,
        val address3: String,
        val latitude: Double,
        val longitude: Double,
        var url: String,
        var promoted: Boolean) : Serializable {

    fun formatDate(): String = SimpleDateFormat("EEEE, MMMM d", Locale.US).format(startDate)

    fun formatTime(): String = String.format(
            Locale.US, "%1\$tl:%1\$tM %1\$tp to %2\$tl:%2\$tM %2\$tp", startDate, endDate)

}
