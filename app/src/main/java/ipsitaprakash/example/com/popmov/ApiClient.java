package ipsitaprakash.example.com.popmov;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ipsitaprakash.example.com.popmov.model.Movie;
import ipsitaprakash.example.com.popmov.service.MovieListDeserializer;
import ipsitaprakash.example.com.popmov.service.TMDbService;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by IpsitaPrakash on 05/07/15.
 */
public class ApiClient
{

    public static TMDbService getTMDBService()
    {

        Type movieListType = new TypeToken<List<Movie>>(){}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(movieListType,new MovieListDeserializer()).create();


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam(TMDbService.API_KEY,TMDbService.API_VALUE);
                    }
                })
                .setEndpoint(TMDbService.BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        TMDbService tmDbService = restAdapter.create(TMDbService.class);
        return tmDbService;
    }

}
