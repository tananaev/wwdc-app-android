package com.tananaev.wwdc.parties

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters

@Database(entities = [Party::class], version = 1)
@TypeConverters(DateAdapter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun partyDao(): PartyDao
}
