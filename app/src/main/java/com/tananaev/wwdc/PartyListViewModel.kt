package com.tananaev.wwdc

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import com.squareup.moshi.Moshi

class PartyListViewModel : ViewModel() {

    companion object {
        private val URL = "https://raw.githubusercontent.com/"
    }

    val users = MutableLiveData<List<Party>>()

    init {

        val moshi = MoshiConverterFactory.create(Moshi.Builder()
                .add(Date::class.java, DateJsonAdapter()).build())

        val retrofit = Retrofit.Builder()
                .baseUrl(URL).addConverterFactory(moshi).build()

        retrofit.create(PartyService::class.java).listParties().enqueue(object: Callback<PartyData> {

            override fun onResponse(call: Call<PartyData>?, response: Response<PartyData>?) {
                if (response?.isSuccessful ?: false) {
                    users.value = response?.body()?.parties?.sortedBy { it.startDate }
                }
            }

            override fun onFailure(call: Call<PartyData>?, t: Throwable?) {
            }

        })
    }

}
