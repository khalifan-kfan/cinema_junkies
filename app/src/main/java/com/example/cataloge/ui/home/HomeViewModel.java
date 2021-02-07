package com.example.cataloge.ui.home;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cataloge.ui.model.AppRepo;
import com.example.cataloge.ui.model.Movie;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> movies;
    private AppRepo repo;
    private  MutableLiveData<Boolean> logged_in;

    public HomeViewModel(Application app) {
        super(app);
        repo = new AppRepo(app);
        movies= new MutableLiveData<>();
        logged_in= new MutableLiveData<>();
        this.logged_in=repo.getLoggedUser();
        this.movies = repo.getMovies();

      //  mText.setValue("This is home fragment");
    }

    public LiveData<List<Movie>> getMovie() {
        movies=(repo.getMovies());
        return movies;
    }

    public MutableLiveData<Boolean> getLogged_in() {
        logged_in = repo.getLoggedUser();
        return logged_in;
    }
}