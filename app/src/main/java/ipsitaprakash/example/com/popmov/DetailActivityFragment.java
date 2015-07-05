package ipsitaprakash.example.com.popmov;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import ipsitaprakash.example.com.popmov.model.Movie;
import ipsitaprakash.example.com.popmov.service.TMDbService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment
{
    private String movieId;
    private Movie movie;
    final String imageBaseUrl = "http://image.tmdb.org/t/p/w500";
    TMDbService tmDbService;

    @Nullable @Bind(R.id.detail_activity_movie_title) TextView titleTextView;
    @Nullable @Bind(R.id.detail_activity_movie_poster) ImageView moviePoster;
    @Nullable @Bind(R.id.detail_activity_movie_release_year)TextView releaseYearTextView;
    @Nullable @Bind(R.id.detail_activity_movie_vote_average)TextView voteAverageTextView;
    @Nullable @Bind(R.id.detail_activity_movie_synopsis)TextView synopsisTextView;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        movieId = getActivity().getIntent().getStringExtra(PopularMoviesFragment.DETAIL_ACTIVITY_MOVIE_ID);
        tmDbService = ApiClient.getTMDBService();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,rootView);
        tmDbService.getMovieDetails(movieId, new Callback<Movie>() {
            @Override
            public void success(Movie movie, Response response)
            {
                if(movie.getTitle().length()>=12)
                {
                    titleTextView.setTextSize(30);
                }
                titleTextView.setText(movie.getTitle());

                Picasso.with(getActivity()).load(imageBaseUrl+movie.getPosterPath()).into(moviePoster);
                releaseYearTextView.setText(movie.getReleaseDate().split("-")[0]);
                voteAverageTextView.setText(movie.getVoteAverage()+"/10");
                synopsisTextView.setText(movie.getSynopsis());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return rootView;
    }
}
