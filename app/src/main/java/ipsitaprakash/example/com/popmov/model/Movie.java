package ipsitaprakash.example.com.popmov.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by IpsitaPrakash on 15/06/15.
 */
public class Movie implements Parcelable
{
    public static final String ID = "id";
    public static final String TITLE="title";
    public static final String RELEASE_DATE = "release_date";
    public static final String POSTER_PATH="poster_path";
    public static final String VOTE_AVERAGE="vote_average";
    public static final String SYNOPSIS="overview";

    @Expose
    @SerializedName(ID)
    private String id;
    @Expose
    @SerializedName(TITLE)
    private String title;
    @Expose
    @SerializedName(RELEASE_DATE)
    private String releaseDate;
    @Expose
    @SerializedName(POSTER_PATH)
    private String posterPath;
    @Expose
    @SerializedName(VOTE_AVERAGE)
    private String voteAverage;
    @Expose
    @SerializedName(SYNOPSIS)
    private String synopsis;

    public Movie(String id, String title, String releaseDate, String posterPath, String voteAverage, String synopsis) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
    }

    private Movie(Parcel in)
    {
        id = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        voteAverage = in.readString();
        synopsis = in.readString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(id);
        out.writeString(title);
        out.writeString(releaseDate);
        out.writeString(posterPath);
        out.writeString(voteAverage);
        out.writeString(synopsis);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
