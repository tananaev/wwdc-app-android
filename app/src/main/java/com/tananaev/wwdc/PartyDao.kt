package com.tananaev.wwdc

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface PartyDao {

    @Query("SELECT * FROM party")
    fun loadData(): LiveData<List<Party>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveData(data: List<Party>)

}
