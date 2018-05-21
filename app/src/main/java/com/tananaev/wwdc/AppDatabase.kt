package com.tananaev.wwdc

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.TypeConverters

@Database(entities = [Party::class], version = 1)
@TypeConverters(DateAdapter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun partyDao(): PartyDao
}
