package com.example.cataloge.ui.booking;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.cataloge.ui.booking.data.CinemaHall;
import com.example.cataloge.ui.booking.data.Days;
import com.example.cataloge.ui.booking.data.MovieTimes;
import com.example.cataloge.ui.model.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingRepo {

    private FirebaseAuth auth;

    Application app;
    private MutableLiveData<List<Days>> data;
   private MutableLiveData<MovieTimes> seat_data;
    private MutableLiveData<List<CinemaHall>> Cinema_data ;
    private MutableLiveData<List<MovieTimes>> time_data;
    private ArrayList<Days> screendays = new ArrayList<>();
    private  ArrayList<CinemaHall> cinemas = new ArrayList<>();
    private ArrayList<MovieTimes> times__ = new ArrayList<>();
   private MovieTimes seats_ = new MovieTimes();


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BookingRepo(Application app) {
        this.app =app ;
        data=new MutableLiveData<>();
        Cinema_data = new MutableLiveData<>();
        seat_data = new MutableLiveData<>();
        time_data = new MutableLiveData<>();
    }


    MutableLiveData<List<Days>> getDays(String id){
        loadDays(id);
       // data.setValue(screendays);
        return data;
    }

    MutableLiveData<List<MovieTimes>> getTime_data(String idT,String movieId,String dayId){
        LoadTimes(idT,movieId,dayId);
        return time_data;
    }
    void LoadTimes(String idT,String movieid,String dayId){
        if(!times__.isEmpty()){
            times__.clear();
        }
        db.collection("Movies").document(movieid).collection("Days")
                .document(dayId).collection("Halls")
                .document(idT).collection("Times")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error == null) {
                            if (!value.isEmpty()) {
                                for (DocumentChange doc : value.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                       // doc.getDocument().toObject(MovieTimes.class).withID(doc.getDocument().getId());
                                        MovieTimes tyms = new MovieTimes();
                                        tyms.setAvailabe_seats((List<Long>) doc.getDocument().get("available_seats"));
                                        tyms.setMovie_time((Date) doc.getDocument().getTimestamp("movie_time").toDate());
                                        tyms.withID(doc.getDocument().getId());
                                        times__.add(tyms);
                                      //  Toast.makeText(app.getBaseContext(),tyms.getAvailabe_seats().get(0).getClass().getName(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                                time_data.setValue(times__);
                            }
                        }
                        Log.d("Repo Error", "onEvent:  ");
                    }
                });
    }

    MutableLiveData<List<CinemaHall>> getCinema_data(String movieid,String dayId){
     loadCinemas(movieid, dayId);
     // Cinema_data.setValue(cinemas);
      return Cinema_data;
    }
    void loadCinemas(String movieid, String dayId){
        if(!cinemas.isEmpty()){
            cinemas.clear();
        }
                db.collection("Movies")
                .document(movieid).collection("Days").document(dayId)
                .collection("Halls")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(error == null) {
                                    if (!value.isEmpty()) {
                                        for (DocumentChange doc : value.getDocumentChanges()) {
                                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                                final CinemaHall halls = doc.getDocument()
                                                        .toObject(CinemaHall.class).withID(doc.getDocument().getId());
                                                //Toast.makeText(app.getBaseContext(), String.valueOf(tymsList.size()), Toast.LENGTH_SHORT).show();
                                                cinemas.add(halls);

                                            }
                                        }
                                        Cinema_data.setValue(cinemas);
                                    } else {
                                        Log.e("Repo", "onEvent: nothing");
                                        Toast.makeText(app.getBaseContext(), "Opps no halls screening", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Log.e("Repo", "onEvent: "+error.getMessage());
                                }
                            }
                        });
    }
    private void loadDays(String id) {
        if(!screendays.isEmpty()){
            screendays.clear();
        }
        db.collection("Movies")
                .document(id).collection("Days")
               .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null) {
                    if (!value.isEmpty()) {
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                final Days days = doc.getDocument()
                                        .toObject(Days.class).withID(doc.getDocument().getId());
                                screendays.add(days);
                            }
                        }
                        data.setValue(screendays);
                    } else {
                        Log.e("Repo", "onEvent: nothing");
                        Toast.makeText(app.getBaseContext(), "Opps no movies screening", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.e("Repo", "onEvent: "+error.getMessage());
                }
            }
        });


    }






    MutableLiveData<MovieTimes> getSeat_data(String mov_id,String day_id,String cinema_id,String tim_id){
        seat_data.setValue(loadAvailableSeats(mov_id,day_id,cinema_id, tim_id));
        return seat_data;
    }
    MovieTimes loadAvailableSeats(String mov_id,String day_id,String cinema_id,String tim_id){
        final MovieTimes[] seats = new MovieTimes[1];
        db.collection("Movies")
                .document(mov_id).collection("Days").document(day_id)
                .collection("Halls").document(cinema_id).collection("Times").document(tim_id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            if(value.exists()){
                                seats[0] = value.toObject(MovieTimes.class).withID(value.getId());
                                //seats[0].setAvailabe_seats((List<Integer>) value.get("available_seats"));
                                //seats[0].setMovie_time((Date) value.getTimestamp("movie_time").toDate());
                                //seats[0].withID(value.getId());
                            }else {
                                Toast.makeText(app.getBaseContext(),"Could'nt seem to find the seats",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(app.getBaseContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return seats[0];
    }
}


