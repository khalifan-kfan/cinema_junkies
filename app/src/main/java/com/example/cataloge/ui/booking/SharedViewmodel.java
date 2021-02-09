package com.example.cataloge.ui.booking;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cataloge.ui.MTN.FixedValues;
import com.example.cataloge.ui.booking.data.CinemaHall;
import com.example.cataloge.ui.booking.data.Days;
import com.example.cataloge.ui.booking.data.MovieTimes;
import com.example.cataloge.ui.model.Movie;

import java.util.HashMap;
import java.util.List;


public class SharedViewmodel extends AndroidViewModel {
    private final MutableLiveData<String> movieID  = new MutableLiveData<String>();
    private MutableLiveData<String> day_id,cinema_id,time_id;
    private  MutableLiveData<Integer> price =new MutableLiveData<>();
    private  HashMap<String, Object> selection = new HashMap<String ,Object>();
    MutableLiveData<List<Long>> time_seats;
    MutableLiveData<List<Days>> days;
    MutableLiveData<List<MovieTimes>> times;
    MutableLiveData<List<CinemaHall>> cinemas ;

    private MutableLiveData<HashMap<String, Object>> selection_live =new MutableLiveData<>();
    private BookingRepo repo;

    public SharedViewmodel(Application app) {
        super(app);
        repo = new BookingRepo(app);

        day_id = new MutableLiveData<>();
        cinema_id = new MutableLiveData<>();
        time_id = new MutableLiveData<>();
        time_seats = new MutableLiveData<>();
        days = new MutableLiveData<>();
        cinemas= new MutableLiveData<>();
        times = new MutableLiveData<>();
    }

    public MutableLiveData<HashMap<String, Object>> getSelection() {
        selection_live.postValue(selection);
        return selection_live;
    }

    public void Add_to_Selection(String key,Object item) {
        selection.put(key,item);
    }

    public MutableLiveData<Integer> getPrice() {
        return price;
    }

    public void setPrice(int pri) {
        price.setValue(pri);
    }

    public void init4(String id){
        this.times = repo.getTime_data(id,movieID.getValue(),day_id.getValue());
    }


    public  void setDay_id(Days day_infor){
        day_id.setValue(day_infor.DocIds);
        Add_to_Selection(FixedValues.date,day_infor.getAbsolute_date());//1
        Add_to_Selection(FixedValues.price,day_infor.getDay_price());//2
        Add_to_Selection(FixedValues.day_id,day_infor.DocIds);//3
    }
    public  void setCinema_id(String id){
        cinema_id.setValue(id);
        Add_to_Selection(FixedValues.cinema_id,id);//4
    }
    public  void setTime_id(MovieTimes time){
        time_id.setValue(time.DocIds);
        this.time_seats.setValue(time.getAvailabe_seats());

        Add_to_Selection(FixedValues.time,time.getMovie_time());//5
        Add_to_Selection(FixedValues.time_id,time.DocIds);//6
    }

    public MutableLiveData<String> getDay_id() {
        return day_id;
    }

    public void setMovieID(Movie movieId,String id){
        movieID.setValue(id);

        Add_to_Selection(FixedValues.Title,movieId.getTitle());//7
        Add_to_Selection(FixedValues.definition,movieId.getDefinition());//8
        Add_to_Selection(FixedValues.pic_link,movieId.getPicture_link());//9
        Add_to_Selection(FixedValues.movie_id,id);//10
    }

    public MutableLiveData<String> getMovieID(){
        return movieID;
    }

    public LiveData<List<Days>> getDays(String id) {
         days = repo.getDays(id);
        return days;
    }
    public LiveData<List<CinemaHall>> getCinemas(String movieid,String dayid) {
        cinemas = repo.getCinema_data(movieid,dayid);
        return cinemas;
    }

    public MutableLiveData<List<MovieTimes>> getCinemaTimes(String cinemaId){

        return times;
    }
    public MutableLiveData<List<Long>> getSeats(){
        return time_seats;
    }

}
