package com.tananaev.wwdc.parties

import retrofit2.Call
import retrofit2.http.GET

interface PartyService {

    @GET("/genadyo/WWDC/master/data/data.json")
    fun listParties(): Call<PartyData>

}
