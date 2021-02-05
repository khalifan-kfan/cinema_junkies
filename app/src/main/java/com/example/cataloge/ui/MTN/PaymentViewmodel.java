package com.example.cataloge.ui.MTN;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;

public class PaymentViewmodel extends AndroidViewModel {

    Application app;
    private MutableLiveData<ProssessMessages> prossessErrorsMutableLiveData;
    private MutableLiveData<Boolean> validity;
    private mtnRepo repo;

    public PaymentViewmodel(@NonNull Application application) {
        super(application);
        this.app = app;
        repo = new mtnRepo(app);
        prossessErrorsMutableLiveData = new MutableLiveData<>();
        validity = new MutableLiveData<>();
    }

    public MutableLiveData<ProssessMessages> getProssessErrorsMutableLiveData() {
        return prossessErrorsMutableLiveData;
    }

    public MutableLiveData<Boolean> getValidity() {
        return validity;
    }

    public void Requesttopay(HashMap<String,String> collection){
        HashMap<String,Object> result = new HashMap<>();
        result=repo.RequestToPay(collection);
        int key = (int) result.get("key");
        if((Boolean) result.get("success")){
            prossessErrorsMutableLiveData.setValue( new ProssessMessages(true,(String) result.get("message")));
        }else if (key==1){
            prossessErrorsMutableLiveData.setValue(new ProssessMessages((String) result.get("message"),null,null));
        }else if (key==2){
            prossessErrorsMutableLiveData.setValue(new ProssessMessages(null,(String) result.get("message"),null));
        }else if(key==3){
            prossessErrorsMutableLiveData.setValue(new ProssessMessages(null,null,(String) result.get("message")));
        }
    }

    void NumberChanged(String number){
        validity.setValue(isNumberValid(number));
    }

    private boolean isNumberValid(String number) {
        return number != null && number.trim().length() > 9;
    }
}
