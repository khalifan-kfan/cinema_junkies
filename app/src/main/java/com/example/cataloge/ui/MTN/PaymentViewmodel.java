package com.example.cataloge.ui.MTN;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cataloge.Services.TransactionsStatusService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ug.sparkpl.momoapi.models.Balance;
import ug.sparkpl.momoapi.models.Transaction;
import ug.sparkpl.momoapi.network.MomoApiException;
import ug.sparkpl.momoapi.network.RequestOptions;
import ug.sparkpl.momoapi.network.collections.CollectionsClient;

public  class PaymentViewmodel extends AndroidViewModel {

    Application app;
    public static MutableLiveData<ProssessMessages> prossessErrorsMutableLiveData;
    public  MutableLiveData<Boolean> validity;
   // private mtnRepo repo;

    public PaymentViewmodel(@NonNull Application application) {
        super(application);
        this.app = app;
       // repo = new mtnRepo(app);
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
        new ReqestToPay().execute(collection);

    }

    void NumberChanged(String number){
        validity.setValue(isNumberValid(number));
    }
    private boolean isNumberValid(String number) {
        return number != null && number.trim().length() > 9;
    }
     static class ReqestToPay extends AsyncTask<HashMap<String,String>, Void,HashMap<String,Object>> {
        CollectionsClient client;
        @Override
        protected HashMap<String, Object> doInBackground(HashMap<String, String>... hashMaps) {
            RequestOptions opts = RequestOptions.builder()
                    .setCollectionApiSecret(FixedValues.MY_SECRET_API_KEY)
                    .setCollectionPrimaryKey(FixedValues.MY_SECRET_SUBSCRIPTION_KEY)
                    .setCollectionUserId(FixedValues.MYSECRET_USER_ID)

                    .build();
            client = new CollectionsClient(opts);

            HashMap<String , Object> result = new HashMap<>();
            try {

                    String transactionRef = client.requestToPay(hashMaps[0]);
                    //present waiting for confrimation massage
                 prossessErrorsMutableLiveData.postValue(new ProssessMessages(true,transactionRef));
            } catch (MomoApiException e) {
                prossessErrorsMutableLiveData.postValue(new ProssessMessages(null,e.toString(),null));
                //handle momo error
            } catch (IOException e) {
                //network
                prossessErrorsMutableLiveData.postValue(new ProssessMessages(null,null,e.getMessage()));
            }
            return result;
        }
        @Override
        protected void onPostExecute(HashMap<String, Object> stringObjectHashMap) {
            super.onPostExecute(stringObjectHashMap);
        }
    }


}
