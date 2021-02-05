package com.example.cataloge.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Movie extends DocIds implements Parcelable {
    private String Title;
    private String picture_link;
    private String director;
    private String definition;
    private String lead_actors;
    private String description;
    private  String sound;

    @ServerTimestamp
    private Date release_date;
    public Movie(){
    }
    public Movie(String title, String picture_link, String director, String definition,
                 String lead_actors, String description,String sound) {
        Title = title;
        this.sound = sound;
        this.picture_link = picture_link;
        this.director = director;
        this.definition = definition;
        this.lead_actors = lead_actors;
        this.description = description;

    }


    protected Movie(Parcel in) {
        Title = in.readString();
        picture_link = in.readString();
        director = in.readString();
        definition = in.readString();
        lead_actors = in.readString();
        description = in.readString();
        sound = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPicture_link() {
        return picture_link;
    }

    public void setPicture_link(String picture_link) {
        this.picture_link = picture_link;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getLead_actors() {
        return lead_actors;
    }

    public void setLead_actors(String lead_actors) {
        this.lead_actors = lead_actors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        dest.writeString(picture_link);
        dest.writeString(director);
        dest.writeString(definition);
        dest.writeString(lead_actors);
        dest.writeString(description);
        dest.writeString(sound);
    }
}
