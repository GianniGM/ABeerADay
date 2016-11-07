
package com.gmod.gianni.querbeer;

/**
 * Created by Gianni on 05/11/2016.
 */

import com.gmod.gianni.querbeer.model.Beer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface BeerInterface {

    @GET("beer/{query}")
    Call<Beer> searchForBeer(@Path("query") String query, @Query("key") String key, @Query("format") String format);

    @GET("beer/{query}")
    Call<Beer> searchForBeer(@Path("query") String query,@Query("q") String name, @Query("key") String key, @Query("format") String format);

}