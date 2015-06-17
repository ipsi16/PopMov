package ipsitaprakash.example.com.popmov;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import ipsitaprakash.example.com.popmov.model.Movie;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment
{
    private String movieId;
    private Movie movie;
    final String imageBaseUrl = "http://image.tmdb.org/t/p/w500";

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        movieId = getActivity().getIntent().getStringExtra(PopularMoviesFragment.DETAIL_ACTIVITY_MOVIE_ID);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        FetchMovieDetailsTask fetchMovieDetailsTask = new FetchMovieDetailsTask();
        fetchMovieDetailsTask.execute(movieId);
        return rootView;
    }

    private class FetchMovieDetailsTask extends AsyncTask<String,Void,JSONObject>
    {
        @Override
        protected void onPostExecute(JSONObject movieJsonObject)
        {
            //setting views to their values
            try
            {

                TextView titleTextView = (TextView) getActivity().findViewById(R.id.detail_activity_movie_title);
                //shrinking text size in case of long movie titles
                if(movieJsonObject.getString(Movie.TITLE).length()>=12)
                {
                    titleTextView.setTextSize(30);
                }
                titleTextView.setText(movieJsonObject.getString(Movie.TITLE));

                ImageView moviePoster = (ImageView) getActivity().findViewById(R.id.detail_activity_movie_poster);
                Picasso.with(getActivity()).load(imageBaseUrl+movieJsonObject.getString(Movie.POSTER_PATH)).into(moviePoster);
                TextView releaseYearTextView = (TextView) getActivity().findViewById(R.id.detail_activity_movie_release_year);
                releaseYearTextView.setText(movieJsonObject.getString(Movie.RELEASE_DATE).split("-")[0]);
                TextView voteAverageTextView = (TextView) getActivity().findViewById(R.id.detail_activity_movie_vote_average);
                voteAverageTextView.setText(movieJsonObject.getString(Movie.VOTE_AVERAGE)+"/10");
                TextView synopsisTextView = (TextView) getActivity().findViewById(R.id.detail_activity_movie_synopsis);
                synopsisTextView.setText(movieJsonObject.getString(Movie.SYNOPSIS));

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            super.onPostExecute(movieJsonObject);
        }

        @Override
        protected JSONObject doInBackground(String... params)
        {

            HttpURLConnection urlConnection = null;
            JSONObject movieJsonObject=null;

            final String BASE_URL = "https://api.themoviedb.org/3/movie/"+params[0];
            Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PopularMoviesFragment.API_PARAM,PopularMoviesFragment.api_key).build();
            try
            {
                URL url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream ==null)
                    return null;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer movieDetailsJsonBuffer = new StringBuffer();

                String line;
                while((line=bufferedReader.readLine())!=null)
                {
                    movieDetailsJsonBuffer.append(line+"\n");
                }
                if(movieDetailsJsonBuffer.length()==0)
                    return null;
                String movieDetailsJsonString = movieDetailsJsonBuffer.toString();
                Log.v("FETCHTEST",movieDetailsJsonString);

                movieJsonObject = getMovieObjectFromJson(movieDetailsJsonString);


            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return movieJsonObject;
        }
    }

    public JSONObject getMovieObjectFromJson(String movieDetailJsonString) throws JSONException {
        if(movieDetailJsonString.length()==0||movieDetailJsonString==null)
            return null;
        JSONObject movieJSonObject = new JSONObject(movieDetailJsonString);
        return movieJSonObject;

    }


}
