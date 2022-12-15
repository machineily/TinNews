package com.laioffer.tinnews.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.network.NewsApi;
import com.laioffer.tinnews.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {

    private final NewsApi newsApi;

    public NewsRepository() {
        newsApi = RetrofitClient.newInstance().create(NewsApi.class);
    }

    public LiveData<NewsResponse> getTopHeadlines(String country) {
        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
        Call<NewsResponse> responseCall = newsApi.getTopHeadlines(country, 40);
        // new a task, make the call<NewsResponse>
        // add task to queue
        // while(true) { retrofit keep check the queue }
        // if queue has `task`, retrofit do task: call endpoint, parse json , etc
        // once retrofit finish the task
        // callback.onResponse(response)
        // if (task success) newsResponseCallback.onResponse()
        // else newsResponseCallback.onFailure()
        Callback<NewsResponse> newsResponseCallback = new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    topHeadlinesLiveData.setValue(response.body());
                    Log.d("getTopHeadlines", response.body().toString());
                } else {
                    topHeadlinesLiveData.setValue(null);
                    Log.d("getTopHeadlines", response.toString());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                topHeadlinesLiveData.setValue(null);
                Log.d("getTopHeadlines", t.toString());
            }
        };

        responseCall.enqueue(newsResponseCallback);
        return topHeadlinesLiveData;
    }

    public LiveData<NewsResponse> searchNews(String query) {
        MutableLiveData<NewsResponse> everyThingLiveData = new MutableLiveData<>();
        newsApi.getEverything(query, 40)
                .enqueue(
                        new Callback<NewsResponse>() {
                            @Override
                            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                                if (response.isSuccessful()) {
                                    everyThingLiveData.setValue(response.body());
                                } else {
                                    everyThingLiveData.setValue(null);
                                }
                            }

                            @Override
                            public void onFailure(Call<NewsResponse> call, Throwable t) {
                                everyThingLiveData.setValue(null);
                            }
                        });
        return everyThingLiveData;
    }

}
