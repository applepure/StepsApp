package com.simpler.myapplication.listeners;

import com.simpler.myapplication.model.JsonCommentsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitListener {

    @GET("comments")
    Call<List<JsonCommentsModel>> getComments(@Query("id") List<Integer> ids);
}
