package com.example.cataloge.ui.MTN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.cataloge.MainActivity;
import com.example.cataloge.R;
import com.example.cataloge.Services.TransactionsStatusService;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class MtnPayActivities extends AppCompatActivity {
    TextView title,date,time,price_per,definition,seat_,amount;
    ImageView thumbnail;
  
    ArrayList<Integer> seats_list;
    EditText number;
    ProgressBar loading;
    Button reqtopay;
    PaymentViewmodel viewmodel;
    PaymentViewModelFactory factory;
    HashMap< String , Object> information = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtn_pay_activities);
        factory = new PaymentViewModelFactory(this.getApplication());
        viewmodel = new ViewModelProvider(this,factory).get(PaymentViewmodel.class);
        // get all the required arguments
        Intent i = getIntent();
        /* title definition price date time cinema seats amount pic link*/
        DateFormat day = new SimpleDateFormat("EE");
        information = (HashMap<String, Object>) i.getSerializableExtra("Information");

        String cinema = (String) information.get(FixedValues.cinema);

        seats_list= new ArrayList<>();
        seats_list = (ArrayList<Integer>) information.get("seats");

        loading = findViewById(R.id.progressBar2);
        reqtopay = findViewById(R.id.effect_payment);
        number = findViewById(R.id.MobileMoneyPhone);
        thumbnail = findViewById(R.id.movie_pick);
        title= findViewById(R.id.title_movie);
        date = findViewById(R.id.date_movie);
        time = findViewById(R.id.time_movie);
        price_per = findViewById(R.id.price_per);
        definition = findViewById(R.id.definition);
        seat_= findViewById(R.id.seats_chosen);
        amount = findViewById(R.id.total_amount);
      
        Glide
                .with(this)
                .load((String) information.get(FixedValues.pic_link))
                .centerCrop()
                .placeholder(R.drawable.movie_placeholder)
                .into(thumbnail);

        title.setText((String) information.get(FixedValues.Title));

        date.setText(String.format("%s %s", new SimpleDateFormat("MMM dd,", Locale.getDefault())
                        .format((Date) information.get(FixedValues.date)), day.format((Date) information.get(FixedValues.date))));
        SimpleDateFormat tym = new SimpleDateFormat("HH:mm");
        time.setText(tym.format((Date) information.get(FixedValues.time)));

        price_per.setText(String.valueOf((int) information.get(FixedValues.price)));
        definition.setText((String) information.get(FixedValues.definition));
        amount.setText(MessageFormat.format("Shs{0}", String.valueOf((int) information.get(FixedValues.amount))));

        seat_.setText(MessageFormat.format("{0} Seats", String.valueOf(seats_list.size())));
        //ui has been set with data 
        viewmodel.getValidity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean) {
                 
                    number.setError("number still invalid");
                }
                reqtopay.setEnabled(aBoolean);
            }
        });
        viewmodel.getProssessErrorsMutableLiveData().observe(this, new Observer<ProssessMessages>() {
            @Override
            public void onChanged(ProssessMessages prossessMessages) {
                if(prossessMessages.isSent_sussessfully()){
                    //send user back to home page
                    //further tasks to be completed in service (background) on a background thread like getting transaction status
                    //recording transaction, removing chosen seats from database and making the ticket which is sent to firebase
                    // returns transaction referance
                    information.put(FixedValues.RefId,prossessMessages.getSuccess());//16
               
                     //       "Fill in your pin and check the dashboard for ticket if success full", Toast.LENGTH_LONG).show();
                    Intent back = new Intent(MtnPayActivities.this, MainActivity.class);
                    Intent startServicer = new Intent(MtnPayActivities.this, TransactionsStatusService.class);
                    startServicer.putExtra("Information",information);
                    startService(startServicer);
                    startActivity(back);
                    finish();
                }else if(prossessMessages.getBalance_error()!=null){
                    Toast.makeText(MtnPayActivities.this, prossessMessages.getBalance_error(), Toast.LENGTH_SHORT).show();
                }else if(prossessMessages.getMomo_excption()!=null){
                    Toast.makeText(MtnPayActivities.this, prossessMessages.getMomo_excption(), Toast.LENGTH_SHORT).show();
                }else if(prossessMessages.getNetwork_issue()!=null){
                    Toast.makeText(MtnPayActivities.this, prossessMessages.getNetwork_issue(), Toast.LENGTH_SHORT).show();
                }
                loading.setVisibility(View.GONE);

            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
            @Override
            public void afterTextChanged(Editable s) {
                viewmodel.NumberChanged(number.getText().toString());
            }
        };
        number.addTextChangedListener(afterTextChangedListener);
        //view model watches results of number to activate button only if the number is valid and full
       String Amount = String.valueOf((Integer) information.get(FixedValues.amount));
        reqtopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String externalId = UUID.randomUUID().toString();
                HashMap<String, String> collmap = new HashMap<>();
                collmap.put("amount", Amount);
                collmap.put("mobile", number.getText().toString());
                collmap.put("externalId", externalId);
                collmap.put("payeeNote", "Cinema");
                collmap.put("payerMessage", "enjoy");
                viewmodel.Requesttopay(collmap);
                loading.setVisibility(View.VISIBLE);
            }
        });



    }
}
