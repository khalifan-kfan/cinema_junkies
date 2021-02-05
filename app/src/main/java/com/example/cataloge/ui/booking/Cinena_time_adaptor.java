package com.example.cataloge.ui.booking;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cataloge.R;
import com.example.cataloge.ui.booking.data.CinemaHall;
import com.example.cataloge.ui.booking.data.MovieTimes;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Cinena_time_adaptor  extends RecyclerView.Adapter<Cinena_time_adaptor.ViewHolder>{

    private List<CinemaHall> my_halls = new ArrayList<>();
    private cinema_time_Fragment ctx;
    private moreORless moreorless;

    public Cinena_time_adaptor(cinema_time_Fragment context) {
        ctx = context;
        moreorless = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cinema_list,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(my_halls.get(position).getName());

        //expand movie times
        holder.Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(!holder.name.getText().toString().toLowerCase().contains("pick")){
                        holder.Iv.setImageResource(R.drawable.less_);
                        holder.name.setBackgroundResource(R.color.teal_700);
                        holder.name.setText(MessageFormat.format("Pick From{0}", my_halls.get(position).getName()));
                        // call interface to open
                        moreorless.openOrclose(my_halls.get(position).DocIds,my_halls.get(position).getName());
                    }else {
                        holder.name.setBackgroundResource(R.color.teal_200);
                        holder.name.setText(my_halls.get(position).getName());
                        holder.Iv.setImageResource(R.drawable.more_);
                        //call interface to close view
                        moreorless.openOrclose(null,null);
                    }
            }
        });



    }
    public void setData(List<CinemaHall> hall){
        this.my_halls = hall;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return my_halls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Iv;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Iv = itemView.findViewById(R.id.more_less);
            name = itemView.findViewById(R.id.cinema_);
        }
    }
}
interface  moreORless{
    void openOrclose(String id,String name);
}

