package com.example.cataloge.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cataloge.MainActivity;
import com.example.cataloge.R;
import com.example.cataloge.ui.booking.BookingActivity;
import com.example.cataloge.ui.logreg.LoginActivity;
import com.example.cataloge.ui.logreg.UserStateChange;
import com.example.cataloge.ui.model.Movie;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements UserStateChange {

    private HomeViewModel homeViewModel;
    private HomeViewModelFactory factory;
    MovieAdaptor adaptor;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        factory = new HomeViewModelFactory(getActivity().getApplication());
        homeViewModel = new ViewModelProvider(this,factory).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView v = root.findViewById(R.id.list_movies);
        v.setLayoutManager(new LinearLayoutManager(container.getContext()));
        v.setHasFixedSize(true);
        adaptor = new MovieAdaptor(container.getContext());
        v.setAdapter(adaptor);

        homeViewModel.getLogged_in().observe((LifecycleOwner) container.getContext(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                adaptor.setLUser_status(aBoolean);
            }
        });

        homeViewModel.getMovie().observe((LifecycleOwner) container.getContext(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Toast.makeText(getContext(),"loaded",Toast.LENGTH_SHORT).show();
                adaptor.setList(movies);
            }
        });
       v.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Boolean atBottom = !recyclerView.canScrollVertically(-1);
                if (atBottom) {
                }
                if (dy > 0 && MainActivity.navView.isShown()) {
                    MainActivity.navView.setVisibility(View.GONE);
                } else if (dy < 0 ) {
                    MainActivity.navView.setVisibility(View.VISIBLE);
                }
            }
        });


        return root;
    }

    @Override
    public void makeStateTrue() {
        adaptor.setLUser_status(true);
    }
}



class MovieAdaptor extends RecyclerView.Adapter<MovieAdaptor.ViewHolder>{
    List<Movie> value= new ArrayList<>();
    Context ctx;
    Boolean isLoggedin;


    public MovieAdaptor( Context context) {
        ctx = context;
      this.isLoggedin = false;
    }


    @NonNull
    @Override
    public MovieAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx)
                .inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdaptor.ViewHolder holder, int position) {
        holder.description.setText(value.get(position).getDescription());
        holder.title.setText(MessageFormat.format("Title:{0}", value.get(position).getTitle()));
        holder.releasedate.setText(MessageFormat.format("released:{0} ", new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                .format(value.get(position).getRelease_date())));
        holder.director.setText(MessageFormat.format("Director: {0}", value.get(position).getDirector()));
        holder.dimention.setText(value.get(position).getDefinition());
        holder.sound.setText(value.get(position).getSound());
        holder.lead.setText(MessageFormat.format("LeadRole:{0}", value.get(position).getLead_actors()));
        holder.description.setMovementMethod(LinkMovementMethod.getInstance());



        Glide
                .with(ctx)
                .load(value.get(position).getPicture_link())
                .centerCrop()
                .placeholder(R.drawable.movie_placeholder)
                .into(holder.photo);
        holder.view_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  display a dialog to ensure only the people signed in go on

                if(!isLoggedin) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setTitle("Login");
                    builder.setMessage("Please login to continue!");
                    builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //go to log in page
                            Intent i = new Intent(ctx, LoginActivity.class);
                            ctx.startActivity(i);
                        }
                    });

                    builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    //Toast.makeText(ctx,"and"+value.get(position).DocIds,Toast.LENGTH_SHORT).show();
                   // move ito booking activity
                    Intent book = new Intent(ctx, BookingActivity.class);
                    book.putExtra("MovieID", value.get(position));
                    book.putExtra("ID",value.get(position).DocIds);
                    ctx.startActivity(book);
                }
            }
        });
    }
    public void setList(List<Movie> list){
        this.value =  list;
        this.notifyDataSetChanged();
    }
    public void setLUser_status(Boolean check){
        this.isLoggedin = check;
    }

    @Override
    public int getItemCount() {
        return value.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title,releasedate,director,dimention,sound,lead;
        public final Button view_dates;
        public final TextView description;
        public final ImageView photo;
        public final CardView thecard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            photo = view.findViewById(R.id.movie);
            thecard = view.findViewById(R.id.movie_card);
            title = view.findViewById(R.id.title);
            releasedate = view.findViewById(R.id.release_date);
            director= view.findViewById(R.id.director);
            dimention = view.findViewById(R.id._3d);
            sound = view.findViewById(R.id._sound);
            // for trailor links in discription
            //text= view.findViewById(R.id.Descriptions);
            //text.setMovementMethod(LinkMovementMethod.getInstance());
            lead = view.findViewById(R.id.leads);
            view_dates = view.findViewById(R.id.button_book);
            description = view.findViewById(R.id.Descriptions);//expands when clicked

        }
    }
}