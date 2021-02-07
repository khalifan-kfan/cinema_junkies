package com.example.cataloge.ui.dashboard.data;

import com.example.cataloge.ui.model.DocIds;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class Ticket extends com.example.cataloge.ui.model.DocIds{

    private String transactionref;//st
   private String userID;//string
    private String username;//st
   private String cinema;//st
    private List<Long> seats;//list
    private int  amount;//int
    @ServerTimestamp
    private Date date;//time stamp
    @ServerTimestamp
    private Date time;//date

    private  int price;// int
    private String day_id;//st
    private String cinema_id;//st
   private String time_id ;//st
   private String Title;//st
    private String definition;//st
    private  String movie_id;//st
    private String pic_link;//st

    public Ticket(String transactionref, String userID, String username, String cinema,
                  List<Long> seats, int amount, int price, String day_id, String cinema_id,
                  String time_id, String title, String definition, String movie_id, String pic_link) {
        this.transactionref = transactionref;
        this.userID = userID;
        this.username = username;
        this.cinema = cinema;
        this.seats = seats;
        this.amount = amount;
        this.price = price;
        this.day_id = day_id;
        this.cinema_id = cinema_id;
        this.time_id = time_id;
        Title = title;
        this.definition = definition;
        this.movie_id = movie_id;
        this.pic_link = pic_link;
    }

    public Ticket() {
    }

    public String getTransactionref() {
        return transactionref;
    }

    public void setTransactionref(String transactionref) {
        this.transactionref = transactionref;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCinema() {
        return cinema;
    }

    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    public List<Long> getSeats() {
        return seats;
    }

    public void setSeats(List<Long> seats) {
        this.seats = seats;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDay_id() {
        return day_id;
    }

    public void setDay_id(String day_id) {
        this.day_id = day_id;
    }

    public String getCinema_id() {
        return cinema_id;
    }

    public void setCinema_id(String cinema_id) {
        this.cinema_id = cinema_id;
    }

    public String getTime_id() {
        return time_id;
    }

    public void setTime_id(String time_id) {
        this.time_id = time_id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getPic_link() {
        return pic_link;
    }

    public void setPic_link(String pic_link) {
        this.pic_link = pic_link;
    }
}
