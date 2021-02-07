package com.example.cataloge.ui.booking;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cataloge.R;
import com.example.cataloge.ui.model.Movie;

public class BookingActivity extends AppCompatActivity {

    private SharedViewmodel viewmodel;
    private SharedViewmodelFactory factory;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
      navController = Navigation.findNavController(this, R.id.fragment);
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("MovieID");
        String  id = intent.getStringExtra("ID");
        factory = new SharedViewmodelFactory(this.getApplication());
        viewmodel = new ViewModelProvider(this,factory).get(SharedViewmodel.class);
        //Toast.makeText(BookingActivity.this,id+"and"+movie.DocIds,Toast.LENGTH_SHORT).show();
        viewmodel.setMovieID(movie,id);

       // viewmodel.init1();
       // viewmodel.init();



    }
}