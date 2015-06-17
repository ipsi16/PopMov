package ipsitaprakash.example.com.popmov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesFragment extends Fragment
{
    ArrayList<String> popularMoviesIdList;
    ArrayList<String> popularMoviesPosterPathList;
    ImageAdapter imageAdapter;
    public static final String DETAIL_ACTIVITY_MOVIE_ID = "movie_id";
    public final static String API_PARAM = "api_key";
    public final static String api_key = "5a3c49b2623f0bc04aa639cd6f9dc14a";
    String sortCriteriaValue="popularity" ;

    public PopularMoviesFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_popular_movies_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id==R.id.action_settings)
        {
            Intent settingsIntent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefSortCriteria = pref.getString("sort_criteria","popularity");
        if(!prefSortCriteria.equals(sortCriteriaValue))
        {
            FetchPopularMoviesTask fetchPopularMoviesTask = new FetchPopularMoviesTask();
            fetchPopularMoviesTask.execute();
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        GridView moviesGridView = (GridView) rootView.findViewById(R.id.popular_movies_grid_view);
        imageAdapter = new ImageAdapter(getActivity());
        moviesGridView.setAdapter(imageAdapter);

        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            Toast toast;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent detailsIntent = new Intent(getActivity(), DetailActivity.class);
                detailsIntent.putExtra(DETAIL_ACTIVITY_MOVIE_ID, popularMoviesIdList.get(position));
                startActivity(detailsIntent);

                /*if (toast != null)
                    toast.cancel();
                toast = Toast.makeText(getActivity(), popularMoviesIdList.get(position), Toast.LENGTH_SHORT);
                toast.show();*/
            }
        });

        FetchPopularMoviesTask fetchPopularMoviesTask = new FetchPopularMoviesTask();
        fetchPopularMoviesTask.execute();
        return rootView;

    }

    public class FetchPopularMoviesTask extends AsyncTask<Void,Void,ArrayList<String>>
    {

        private String LOG_TAG =  FetchPopularMoviesTask.class.getSimpleName();

        @Override
        protected void onPostExecute(ArrayList<String> strings)
        {
            popularMoviesPosterPathList = strings;
            imageAdapter.notifyDataSetChanged();
            super.onPostExecute(strings);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params)
        {

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            ArrayList<String> popularMoviePathList = null;

            try
            {
                //Constructing the api query UI
                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
                final String SORT_PARAM = "sort_by";

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sortCriteriaValue = prefs.getString("sort_criteria","popularity");

                String sort_by = sortCriteriaValue+".desc";

                Uri queryUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sort_by)
                        .appendQueryParameter(API_PARAM,api_key)
                        .build();
                URL url = new URL(queryUri.toString());
                Log.v(LOG_TAG,url.toString());

                //Sending the query
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();


                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                if(inputStream==null)
                {
                    return null;
                }
                String line;
                while((line=bufferedReader.readLine())!=null)
                {
                    buffer.append(line+"\n");
                }
                if(buffer.length()==0)
                {
                    return null;
                }
                String popularMoviesJsonString = buffer.toString();

                Log.v(LOG_TAG,popularMoviesJsonString);

                popularMoviePathList = getMovieParameterFromJson(popularMoviesJsonString, "poster_path");
                popularMoviesIdList = getMovieParameterFromJson(popularMoviesJsonString,"id");
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "IO Error", e);
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                Log.e(LOG_TAG, "JSON parsing e", e);
            }
            return popularMoviePathList;
        }



        private ArrayList<String> getMovieParameterFromJson(String moviesJsonString,String parameter) throws JSONException
        {
            ArrayList<String> moviePosterPathList = new ArrayList<String>();

            JSONObject jsonObject = new JSONObject(moviesJsonString);
            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for(int i=0;i<resultsArray.length();i++)
            {
                JSONObject movieDetails = resultsArray.getJSONObject(i);

                String posterPath = movieDetails.getString(parameter);
                moviePosterPathList.add(posterPath);
                Log.v(LOG_TAG,posterPath);
            }

            return moviePosterPathList;
        }
    }

    private class ImageAdapter extends BaseAdapter
    {
        private Context mContext;
        final String imageBaseUrl = "http://image.tmdb.org/t/p/w500";

        public ImageAdapter(Context context)
        {
            super();
            mContext = context;

        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;

            if((popularMoviesPosterPathList!=null||popularMoviesPosterPathList.size()!=0))
            {
                if(convertView==null)
                {
                    imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Log.v("ImageAdapter", position+" "+popularMoviesPosterPathList.get(position));
                }
                else
                {
                    imageView = (ImageView) convertView;
                }
                if(!popularMoviesPosterPathList.get(position).equals("null"))
                {
                    Picasso.with(mContext).load(imageBaseUrl + popularMoviesPosterPathList.get(position)).into(imageView);
                }
                return imageView;
            }
            return convertView;
        }

        @Override
        public int getCount()
        {
            if(popularMoviesPosterPathList==null)
            {
                return 0;
            }
            return popularMoviesPosterPathList.size();
        }



        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }
    }


}
