package com.example.cataloge.ui.logreg;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cataloge.ui.home.HomeViewModel;

public class LoginViewModelFactory implements ViewModelProvider.Factory {
    Application app;
    public LoginViewModelFactory(Application app){
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
       LoginViewModel loginViewModel = new LoginViewModel(app);
        if(modelClass.isAssignableFrom(LoginViewModel.class)){
            return(T) loginViewModel;
        }
        throw new IllegalArgumentException("Factory problem");
    }
}
