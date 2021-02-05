package com.example.cataloge.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class HomeViewModelFactory implements ViewModelProvider.Factory {


    Application app;
    public HomeViewModelFactory(Application app){
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        HomeViewModel homeViewModel = new HomeViewModel(app);
        if(modelClass.isAssignableFrom(HomeViewModel.class)){
            return(T) homeViewModel;
        }
        throw new IllegalArgumentException("Factory problem");
    }
}
