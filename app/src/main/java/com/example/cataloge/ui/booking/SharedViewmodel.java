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
    public void init1() {
        this.days = repo.getDays(movieID.getValue());
    }
    public void init2(){
      this.cinemas = repo.getCinema_data(movieID.getValue(),day_id.getValue());
    }
    public void init3(){
      // this.time_seats= repo.getSeat_data(movieID.getValue(),day_id.getValue(),cinema_id.getValue(),time_id.getValue());
    }
    public void init4(String id){
        this.times = repo.getTime_data(id,movieID.getValue(),day_id.getValue());
    }


    public  void setDay_id(Days day_infor){
        day_id.setValue(day_infor.DocIds);
        Add_to_Selection("date",day_infor.getAbsolute_date());
        Add_to_Selection("price",day_infor.getDay_price());
        Add_to_Selection("day_id",day_infor.DocIds);
    }
    public  void setCinema_id(String id){
        cinema_id.setValue(id);
        Add_to_Selection("cinema_id",id);
    }
    public  void setTime_id(MovieTimes time){
        time_id.setValue(time.DocIds);
        this.time_seats.setValue(time.getAvailabe_seats());

        Add_to_Selection("time",time.getMovie_time());
        Add_to_Selection("time_id",time.DocIds);
    }

    public MutableLiveData<String> getDay_id() {
        return day_id;
    }

    public void setMovieID(Movie movieId,String id){
        movieID.setValue(id);
       // Toast.makeText(getApplication().getBaseContext(),movieId.getTitle(),Toast.LENGTH_SHORT).show();
        Add_to_Selection("Title",movieId.getTitle());
        Add_to_Selection("definition",movieId.getDefinition());
        Add_to_Selection("pic_link",movieId.getPicture_link());
        Add_to_Selection("movie_id",id);
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
        //times = repo.getTime_data(cinemaId,day_id.getValue(),movieID.getValue());
        return times;
    }
    public MutableLiveData<List<Long>> getSeats(){
       //  time_seats= repo.getSeat_data(movieID.getValue(),day_id.getValue(),
         //      cinema_id.getValue(),time_id.getValue());
        return time_seats;
    }

}
