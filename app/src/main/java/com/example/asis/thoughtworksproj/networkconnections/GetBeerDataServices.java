package com.example.asis.thoughtworksproj.networkconnections;

import com.example.asis.thoughtworksproj.model.BeerDatas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by praka on 12/24/2017.
 */

public interface GetBeerDataServices {

    @GET("/beercraft")
    Call<List<BeerDatas>> getAllBeerDatas();
}
