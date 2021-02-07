package com.example.cataloge.ui.model;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppRepo  {
    private Application  app;
    private MutableLiveData<List<Movie>> data;
   private MutableLiveData<FirebaseUser> userMutableLiveData;
    private FirebaseAuth auth;
    private  MutableLiveData<Boolean> loggedUser;
    int success = 1;
    //private MutableLiveData<List<Movie>> data;


    private FirebaseFirestore db ;

    public AppRepo(Application app) {
        this.app = app;
        userMutableLiveData = new MutableLiveData<>();
        loggedUser = new MutableLiveData<>();
        data=new MutableLiveData<>();
        auth=FirebaseAuth.getInstance();
       Context c = app.getBaseContext();
        db = FirebaseFirestore.getInstance();

        if(auth.getCurrentUser()!= null){
            userMutableLiveData.postValue(auth.getCurrentUser());
            loggedUser.postValue(true);
        }else {
            loggedUser.postValue(false);
        }
    }

    public MutableLiveData<List<Movie>> getMovies(){
        loadMovies();
        return data;
    }

    private void loadMovies() {
        List<Movie>  movieslist = new ArrayList<>();
       // Toast.makeText(app.getBaseContext(), "load started", Toast.LENGTH_SHORT).show();
      db.collection("Movies").addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
               public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               if(error == null) {
                   if (!value.isEmpty()) {
                       for (DocumentChange doc : value.getDocumentChanges()) {
                           if (doc.getType() == DocumentChange.Type.ADDED) {
                               final Movie movie = doc.getDocument()
                                       .toObject(Movie.class).withID(doc.getDocument().getId());
                               movieslist.add(movie);
                           }
                       }
                       data.setValue(movieslist);
                   } else {
                       Log.e("Repo", "onEvent: nothing");
                       Toast.makeText(app.getBaseContext(), "Opps no movies screening", Toast.LENGTH_SHORT).show();
                   }
               }else {
                   Log.e("Repo", "onEvent: "+error.getMessage());
               }
           }
       });

    }

    public MutableLiveData<FirebaseUser> register(String email, String pass, String number){
       // final Boolean[] succes = new Boolean[1];
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userMutableLiveData.postValue(auth.getCurrentUser());
                          //  succes[0] =true;
                            Map<String, String> InforMap = new HashMap<>();
                            InforMap.put("email",email);
                            InforMap.put("number",number);
                            db.collection("Users").document(auth.getCurrentUser().getUid()).set(InforMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(app,"Information saved",Toast.LENGTH_SHORT).show();
                                            } else {
                                              //retry saving
                                            }
                                        }

                                    });
                        }else{
                            Toast.makeText(app,task.getException().toString(),Toast.LENGTH_SHORT).show();
                         //   succes[0] =false;
                            userMutableLiveData.postValue(null);
                        }

                    }
                });
        return userMutableLiveData;
    }
    public int login (String email, String  pass){


        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    success = 0;
                }else {
                    Toast.makeText(app.getBaseContext(), "Error : " + task.getException()
                            .getMessage(), Toast.LENGTH_LONG).show();
                    success = 1;
                }

            }
        });

        return success;
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void logOut(){
        auth.signOut();
        loggedUser.postValue(false);
    }


    public MutableLiveData<Boolean> getLoggedUser() {
        return loggedUser;
    }
}
