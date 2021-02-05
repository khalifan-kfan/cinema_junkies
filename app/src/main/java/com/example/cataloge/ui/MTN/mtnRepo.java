package com.example.cataloge.ui.MTN;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import ug.sparkpl.momoapi.models.Balance;
import ug.sparkpl.momoapi.network.MomoApiException;
import ug.sparkpl.momoapi.network.RequestOptions;
import ug.sparkpl.momoapi.network.collections.CollectionsClient;


public class mtnRepo {
    Application app;

    CollectionsClient client;
    public mtnRepo(Application app) {
        this.app =app ;

        RequestOptions opts = RequestOptions.builder()
                .setCollectionApiSecret(FixedValues.MY_SECRET_API_KEY)
                .setCollectionPrimaryKey(FixedValues.MY_SECRET_SUBSCRIPTION_KEY)
                .setCollectionUserId(FixedValues.MYSECRET_USER_ID)
                .build();
       client = new CollectionsClient(opts);

    }
    HashMap<String,Object> RequestToPay(HashMap<String,String> coll) {
        HashMap<String , Object> result = new HashMap<>();

        try {
            Balance bal = client.getBalance();
            if(Integer.parseInt(bal.getBalance()) > Integer.parseInt(coll.get("amount"))){
                String transactionRef = client.requestToPay(coll);
                //present waiting for confrimation massage
                result.put("key",0);
                result.put("message",transactionRef);
                result.put("success",true);
            }else{
                result.put("key",1);
                result.put("message","less balance");
                result.put("success",false);
            //present not enough balance
            }
        } catch (MomoApiException e) {
            result.put("key",2);
            result.put("message",e.toString());
            result.put("success",false);
            //handle momo error
        } catch (IOException e) {
            //network
            result.put("key",3);
            result.put("message",e.toString());
            result.put("success",false);
        }

        return result;
    }




}
