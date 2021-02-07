package com.example.cataloge.ui.dashboard;

import android.text.BoringLayout;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cataloge.ui.dashboard.data.Ticket;
import com.example.cataloge.ui.model.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.ResourceDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<List<Ticket>> myTicket;
    private List<Ticket> ticketList;
    private MutableLiveData<Boolean> loggedUser;
    private FirebaseAuth auth;
    FirebaseFirestore db;

    public DashboardViewModel() {
        myTicket = new MutableLiveData<>();
        loggedUser = new MutableLiveData<>();
        ticketList = new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!= null){
            loggedUser.postValue(true);
            getTickets();
        }else {
            loggedUser.postValue(false);
        }
       // initiate fetch here

    }

    public MutableLiveData<Boolean> getLoggedUser() {
        return loggedUser;
    }
    public MutableLiveData<List<Ticket>> getMyTicket() {
        if( myTicket!=null) {
            return myTicket;
        }else return null;
    }
    private void getTickets() {
        //get tickets
        if(!ticketList.isEmpty()){
            ticketList.clear();
        }
        db.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("Tickets")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error == null) {
                    if (!value.isEmpty()) {
                        for (DocumentChange doc : value.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                final Ticket ticket= doc.getDocument()
                                        .toObject(Ticket.class).withID(doc.getDocument().getId());
                                ticketList.add(ticket);
                            }
                        }
                        myTicket.setValue(ticketList);
                    } else {
                        Log.e("Repo", "onEvent: no tickets");
                    }
                }else {
                    Log.e("Repo", "onEvent: "+error.getMessage());
                }
            }
        });
    }

    public void sendToHistory(Ticket ticket) {
        db.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .collection("History").document(ticket.DocIds).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().exists()){
                        db.collection("Users").document(auth.getCurrentUser().getUid())
                                .collection("History").document(ticket.DocIds).set(ticket).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //confirm
                                            Log.d("dashboard", "onComplete: was a successfully to history" +
                                                    "");
                                            // now delete the ticket
                                            db.collection("Users").document(ticket.getUserID())
                                                    .collection("Tickets").document(ticket.DocIds).delete();

                                        }
                                    }
                                }
                        );
                    }
                }
            }
        });
    }
}