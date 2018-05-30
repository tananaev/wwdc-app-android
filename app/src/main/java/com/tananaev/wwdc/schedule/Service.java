package com.tananaev.wwdc.schedule;

import com.tananaev.wwdc.schedule.model.Data;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    @GET("/contents.json")
    Call<Data> getData();

}
