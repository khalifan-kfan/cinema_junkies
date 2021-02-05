package com.example.cataloge.ui.MTN;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cataloge.ui.booking.SharedViewmodel;
import com.example.cataloge.ui.home.HomeViewModel;

public class PaymentViewModelFactory implements ViewModelProvider.Factory {

    Application app;
    public PaymentViewModelFactory(Application app){
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
       PaymentViewmodel pvm = new PaymentViewmodel(app);
        if(modelClass.isAssignableFrom(PaymentViewmodel.class)){
            return(T) pvm;
        }
        throw new IllegalArgumentException("payment Factory problem");
    }

}
