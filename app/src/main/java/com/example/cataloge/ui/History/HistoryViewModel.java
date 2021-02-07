package com.example.cataloge.ui.History;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cataloge.ui.dashboard.data.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends ViewModel {
    private MutableLiveData<List<Ticket>> myHTicket;
    private List<Ticket> HistticketList;
    private MutableLiveData<Boolean> loggedUser;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    public HistoryViewModel() {
        myHTicket = new MutableLiveData<>();
        loggedUser = new MutableLiveData<>();
        HistticketList = new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!= null){
            loggedUser.postValue(true);
            getHistory();
        }else {
            loggedUser.postValue(false);
        }
        // initiate fetch here
    }

    public MutableLiveData<List<Ticket>> getMyHTicket() {
        if( myHTicket!=null) {
            return myHTicket;
        }else return null;
    }

    public MutableLiveData<Boolean> getLoggedUser() {
        return loggedUser;
    }

    private void getHistory() {

        //get tickets
        if(!HistticketList.isEmpty()){
            HistticketList.clear();
        }
        db.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("History")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error == null) {
                            if (!value.isEmpty()) {
                                for (DocumentChange doc : value.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        final Ticket ticket= doc.getDocument()
                                                .toObject(Ticket.class).withID(doc.getDocument().getId());
                                        HistticketList.add(ticket);
                                    }
                                }
                                myHTicket.setValue(HistticketList);
                            } else {
                                Log.e("Repo", "onEvent: no hist");
                            }
                        }else {
                            Log.e("Repo", "onEvent: "+error.getMessage());
                        }
                    }
                });

    }


}