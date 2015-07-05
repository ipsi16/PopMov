package ipsitaprakash.example.com.popmov.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ipsitaprakash.example.com.popmov.model.Movie;

/**
 * Created by IpsitaPrakash on 05/07/15.
 */
public class MovieListDeserializer implements JsonDeserializer<List<Movie>>
{

    @Override
    public List<Movie> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonArray movieList = json.getAsJsonObject().getAsJsonArray("results");
        Type listType = new TypeToken<List<Movie>>(){}.getType();
        return new Gson().fromJson(movieList.toString(),listType);
    }
}
