package com.tananaev.wwdc

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Room
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import com.squareup.moshi.Moshi
import java.util.concurrent.Executors

class PartyListViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val URL = "https://raw.githubusercontent.com/"
    }

    val parties = MutableLiveData<List<Party>>()

    val database = Room.databaseBuilder(
            getApplication(), AppDatabase::class.java, "parties").build()

    init {
        database.partyDao().loadData().observeForever {
            if (it?.isEmpty() == false) {
                parties.postValue(it)
            } else {
                refresh()
            }
        }
    }

    fun refresh() {
        val json = MoshiConverterFactory.create(Moshi.Builder()
                .add(Date::class.java, DateAdapter()).build())

        val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(json)
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build()

        retrofit.create(PartyService::class.java).listParties().enqueue(object: Callback<PartyData> {

            override fun onResponse(call: Call<PartyData>?, response: Response<PartyData>?) {
                if (response?.isSuccessful == true) {
                    val result = response.body()?.parties?.sortedBy { it.startDate }
                    if (result != null) {
                        database.partyDao().saveData(result)
                        parties.postValue(result)
                    }
                } else {
                    onFailure(null, null)
                }
            }

            override fun onFailure(call: Call<PartyData>?, t: Throwable?) {
                Toast.makeText(getApplication(), R.string.service_error, Toast.LENGTH_LONG).show()
            }

        })

    }

}
