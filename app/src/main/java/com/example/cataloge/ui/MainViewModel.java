package com.example.cataloge.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cataloge.ui.model.AppRepo;
import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends AndroidViewModel {

    private  AppRepo repo;
    private MutableLiveData<FirebaseUser> user;
    private  MutableLiveData<Boolean> loggedout;



    public MainViewModel(@NonNull Application application) {
        super(application);
        repo = new AppRepo(application);
        user = new MutableLiveData<>();
        loggedout = new MutableLiveData<>();
    }
    public void logOut(){
     repo.logOut();

    }
    public MutableLiveData<FirebaseUser> getUser() {
        return user;
    }

    public MutableLiveData<Boolean> getLoggedout() {
        return loggedout;
    }
}
