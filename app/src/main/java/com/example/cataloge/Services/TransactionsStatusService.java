package com.example.cataloge.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cataloge.ui.MTN.FixedValues;
import com.example.cataloge.ui.MTN.ProssessMessages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ug.sparkpl.momoapi.models.Transaction;
import ug.sparkpl.momoapi.network.MomoApiException;
import ug.sparkpl.momoapi.network.RequestOptions;
import ug.sparkpl.momoapi.network.collections.CollectionsClient;

import static com.example.cataloge.ui.MTN.FixedValues.MY_SECRET_SUBSCRIPTION_KEY;

public class TransactionsStatusService extends Service {


    HashMap<String,Object> informap = new HashMap<>();
    Handler handler= new Handler() ;

    CollectionsClient client;
    RequestOptions opts;
    int i =0;
    ArrayList<Long> full_seats_list = new ArrayList<>();//full list to be queried
    ArrayList<Integer> seats = new ArrayList();//Seats that were chosen

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String  userID = auth.getCurrentUser().getUid();
    FirebaseUser user;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        opts  = RequestOptions.builder()
                .setCollectionApiSecret(FixedValues.MY_SECRET_API_KEY)
                .setCollectionPrimaryKey(MY_SECRET_SUBSCRIPTION_KEY)
                .setCollectionUserId(FixedValues.MYSECRET_USER_ID)
                .build();
        client = new CollectionsClient(opts);

        informap = (HashMap<String, Object>) intent.getSerializableExtra("Information");
        String  ref = (String) informap.get(FixedValues.RefId);
        //put network operation in back thead
       // without  that it will produce main thread  error
        Runnable k = new Runnable() {
            @Override
            public void run() {
                //check reference id
                try {
                    Transaction t = client.getTransaction(ref);
                    String state =  t.getStatus();
                    
                    if(state.toLowerCase().contains("success")){
                        //end process
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"transaction:"+state,Toast.LENGTH_SHORT).show();
                                UpdateSeats();
                            }
                        });

                    }else if(state.toLowerCase().contains("failed")||state.toLowerCase().contains("rejected")){
                        //end process
                        handler.post(() -> Toast.makeText(getApplicationContext(),"transaction:"+state,Toast.LENGTH_SHORT).show());
                        stopSelf();
                    }else {
                        //assuming process is pending
                        //try again after 5 seconds
                        handler.postDelayed(this, 5000);
                    }
                } catch (IOException e ) {
                    e.printStackTrace();
                    //it should only try 10 times,
                    if(i<10) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        handler.postDelayed(this, 10000);
                    }else {
                        Toast.makeText(getApplicationContext(),"Process failed,transaction terminated",Toast.LENGTH_SHORT).show();
                        stopSelf();
                    }
                    i++;
                }
            }
        };
        Thread bac = new Thread(k);
        bac.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void UpdateSeats() {

       //first remove picked seats
        seats = (ArrayList<Integer>) informap.get("seats");
        DocumentReference seatsdoc = firestore.collection("Movies")
                .document((String) informap.get("movie_id"))
                .collection("Days").document((String) informap.get("day_id"))
                .collection("Halls").document((String) informap.get("cinema_id"))
                .collection("Times").document((String) informap.get("time_id"));
        Task<DocumentSnapshot> doc = seatsdoc.get();
        doc.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           if(task.isSuccessful()) {
                full_seats_list= (ArrayList<Long>) task.getResult().get("available_seats");
                for(int seat=0;seat<seats.size();seat++){
                        full_seats_list.remove(new Long(seats.get(seat)));
               }
                //repost new list
               seatsdoc.update("available_seats",full_seats_list).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                          recordTransaction();
                        }else {
                            //go back up
                        }
                   }
               });
           }else {
               Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
               //repeat from the top
           }
            }
        });

    }
    private void recordTransaction() {
        //first for the user's record
        informap.put(FixedValues.username,auth.getCurrentUser().getEmail());//14
        informap.put(FixedValues.userId,userID);//15
        firestore.collection("Users").document(userID)
                .collection("Tickets").add(informap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
             if (task.isSuccessful()) {
                 //then for data keeping
                 firestore.collection("Transaction")
                         .add(informap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentReference> task) {
                       if(task.isSuccessful()) {
                           Toast.makeText(getApplicationContext(),"Process Completed" +
                                   "Please check your dash board for the ticket ",Toast.LENGTH_SHORT).show();
                       }
                       stopSelf();//end service
                     }
                 });
             }else {
                 //retry
             }
            }
        });

    }
  
}
