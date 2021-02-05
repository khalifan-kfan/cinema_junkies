package com.example.cataloge.ui.booking.data;

import com.example.cataloge.ui.model.DocIds;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieTimes extends com.example.cataloge.ui.model.DocIds {

    private List<Long> available_seats;
    @ServerTimestamp
    private Date movie_time;

    public MovieTimes() {
    }

    public MovieTimes(List<Long>available_seats) {
        this.available_seats = available_seats;
    }

    public List<Long> getAvailabe_seats() {
        return available_seats;
    }

    public void setAvailabe_seats(List<Long> available_seats) {
        this.available_seats = available_seats;
    }

    public Date getMovie_time() {
        return movie_time;
    }

    public void setMovie_time(Date movie_time) {
        this.movie_time = movie_time;
    }
}
