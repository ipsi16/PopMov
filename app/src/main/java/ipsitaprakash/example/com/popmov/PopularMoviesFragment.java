package ipsitaprakash.example.com.popmov;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
public class PopularMoviesFragment extends Fragment
{
    ArrayList<Movie> popularMoviesList = null;
    ImageAdapter imageAdapter;
    TMDbService tmDbService;
    public static final String DETAIL_ACTIVITY_MOVIE_ID = "movie_id";
    public static final String MOVIE_LIST_KEY = "movie_list";
    String sortCriteriaValue="popularity" ;

    @Nullable @Bind(R.id.popular_movies_grid_view)
    GridView moviesGridView;

    public PopularMoviesFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortCriteriaValue = pref.getString("sort_criteria","popularity");
        imageAdapter = new ImageAdapter(getActivity());
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null||!savedInstanceState.containsKey(MOVIE_LIST_KEY))
        {
            tmDbService = ApiClient.getTMDBService();
            tmDbService.getTopMoviesByCriteria(sortCriteriaValue + ".desc", new FetchSortedMovieListCallback());
        }
        else
            popularMoviesList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);



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

            sortCriteriaValue = prefSortCriteria;
            tmDbService.getTopMoviesByCriteria(sortCriteriaValue+".desc", new FetchSortedMovieListCallback());
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this,rootView);

        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
            moviesGridView.setNumColumns(4);
        else
            moviesGridView.setNumColumns(2);
        moviesGridView.setAdapter(imageAdapter);

        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsIntent = new Intent(getActivity(), DetailActivity.class);
                detailsIntent.putExtra(DETAIL_ACTIVITY_MOVIE_ID, popularMoviesList.get(position).getId());
                startActivity(detailsIntent);

            }
        });


        return rootView;

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

            if((popularMoviesList!=null||popularMoviesList.size()!=0))
            {
                if(convertView==null)
                {
                    imageView = new ImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Log.v("ImageAdapter", position+" "+popularMoviesList.get(position).getPosterPath());
                }
                else
                {
                    imageView = (ImageView) convertView;
                }
                if(!popularMoviesList.get(position).getPosterPath().equals("null"))
                {
                    Picasso.with(mContext).load(imageBaseUrl + popularMoviesList.get(position).getPosterPath()).into(imageView);
                }
                return imageView;
            }
            return convertView;
        }

        @Override
        public int getCount()
        {
            if(popularMoviesList==null)
            {
                return 0;
            }
            return popularMoviesList.size();
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

    public class FetchSortedMovieListCallback implements Callback<List<Movie>>
    {

        @Override
        public void success(List<Movie> movies, Response response)
        {
            popularMoviesList = (ArrayList<Movie>) movies;
            imageAdapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {

        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(popularMoviesList!=null)
            outState.putParcelableArrayList(MOVIE_LIST_KEY,popularMoviesList);
        super.onSaveInstanceState(outState);
    }
}
