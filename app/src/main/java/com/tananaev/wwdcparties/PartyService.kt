package com.tananaev.wwdcparties

import retrofit2.Call
import retrofit2.http.GET

interface PartyService {

    @GET("/genadyo/WWDC/master/data/data.json")
    fun listParties(): Call<PartyData>

}
