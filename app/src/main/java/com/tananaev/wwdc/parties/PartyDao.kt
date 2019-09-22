package com.tananaev.wwdc.parties

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PartyDao {

    @Query("SELECT * FROM party")
    fun loadData(): LiveData<List<Party>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveData(data: List<Party>)

}
