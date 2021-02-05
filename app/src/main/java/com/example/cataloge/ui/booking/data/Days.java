package com.example.cataloge.ui.booking.data;

import com.example.cataloge.ui.model.DocIds;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Days extends com.example.cataloge.ui.model.DocIds {

    private int day_price;
    @ServerTimestamp
    private Date absolute_date;

    public Days(int day_price) {
        this.day_price = day_price;
    }
    Days(){}

    public int getDay_price() {
        return day_price;
    }

    public void setDay_price(int day_price) {
        this.day_price = day_price;
    }

    public Date getAbsolute_date() {
        return absolute_date;
    }

    public void setAbsolute_date(Date absolute_date) {
        this.absolute_date = absolute_date;
    }
}
