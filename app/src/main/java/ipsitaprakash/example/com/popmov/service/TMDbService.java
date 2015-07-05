package ipsitaprakash.example.com.popmov.service;

import java.util.ArrayList;
import java.util.List;

import ipsitaprakash.example.com.popmov.model.Movie;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by IpsitaPrakash on 02/07/15.
 */
public interface TMDbService
{
    public static final String BASE_URL = "https://api.themoviedb.org/3";
    public static final String API_KEY="api_key";
    public static final String API_VALUE = "5a3c49b2623f0bc04aa639cd6f9dc14a";

    @GET("/discover/movie")
    public void getTopMoviesByCriteria(@Query("sort_by")String sortCriteria,Callback<List<Movie>> cb);

    @GET("/movie/{id}")
    public void getMovieDetails(@Path("id")String id,Callback<Movie> cb);

}
