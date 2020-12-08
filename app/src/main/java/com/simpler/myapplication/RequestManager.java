package com.simpler.myapplication;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simpler.myapplication.listeners.ResponseListener;
import com.simpler.myapplication.listeners.RetrofitListener;
import com.simpler.myapplication.model.JsonCommentsModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RequestManager implements Callback<List<JsonCommentsModel>> {
    ResponseListener responseListener;
    Call<List<JsonCommentsModel>> call;
    private final static String BASE_URL = "https://jsonplaceholder.typicode.com/";
    public RequestManager(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public void start(int[] bounds, Handler handler) {//handler is passed to determine if the request should be delayed
        int lower = bounds[0] ;
        int upper = bounds[1] + 1 ;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        List<Integer> ids = new ArrayList<>();
        IntStream.range(lower, upper).forEach(ids::add);// Adding id's to the url's query string param
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitListener retrofitListener = retrofit.create(RetrofitListener.class);

        call = retrofitListener.getComments(ids);// Bonus assignment - making the request after 3000 milis
        if (handler!= null){
            new Handler().postDelayed(() -> call.enqueue(this), 3000);
        }else {
            call.enqueue(this);
        }





    }

    public void cancelRequest() {
        call.cancel();
    }

    @Override
    public void onResponse(@NonNull Call<List<JsonCommentsModel>> call, Response<List<JsonCommentsModel>> response) {
        if(response.isSuccessful()) {
            List<JsonCommentsModel> comments = response.body();
            if (comments != null) {
                responseListener.onResponse(comments);
          }
        } else {
            try {
                if (response.errorBody() != null) {
                    Log.d("ERROR",response.errorBody().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<List<JsonCommentsModel>> call, Throwable t) {
        t.printStackTrace();
    }
}