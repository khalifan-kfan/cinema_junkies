package com.example.cataloge.ui.loginregister;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cataloge.ui.home.HomeViewModel;

public class SignUpViewModelFactory implements ViewModelProvider.Factory {
    Application app;
    public SignUpViewModelFactory(Application app){
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
       SignupviewModel iewModel = new SignupviewModel(app);
        if(modelClass.isAssignableFrom(HomeViewModel.class)){
            return(T) iewModel;
        }
        throw new IllegalArgumentException("Factory problem");
    }
}
