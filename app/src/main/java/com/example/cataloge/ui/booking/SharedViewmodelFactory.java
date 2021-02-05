package com.example.cataloge.ui.booking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cataloge.ui.home.HomeViewModel;

public class SharedViewmodelFactory implements ViewModelProvider.Factory {
    Application app;
    public SharedViewmodelFactory(Application app){
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
       SharedViewmodel sharedViewmodel = new SharedViewmodel(app);
        if(modelClass.isAssignableFrom(SharedViewmodel.class)){
            return(T) sharedViewmodel;
        }
        throw new IllegalArgumentException("shared Factory problem");
    }
}
