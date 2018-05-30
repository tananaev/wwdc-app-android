package com.tananaev.wwdc.schedule

import com.tananaev.wwdc.schedule.model.Data

import retrofit2.Call
import retrofit2.http.GET

interface ScheduleService {

    @get:GET("/contents.json")
    val data: Call<Data>

}
