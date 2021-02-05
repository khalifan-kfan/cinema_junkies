package com.example.cataloge.ui.booking;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cataloge.MainActivity;
import com.example.cataloge.R;
import com.example.cataloge.ui.booking.data.Days;
import com.google.protobuf.StringValue;


import java.sql.Time;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class DayRecyclerViewAdapter extends RecyclerView.Adapter<DayRecyclerViewAdapter.ViewHolder> {

    private List<Days> mValues= new ArrayList<>();
    private final Day_picked_Fragment ctx;

    private goClick listerner;

    public DayRecyclerViewAdapter(Day_picked_Fragment ctx){

        this.ctx = ctx;
        listerner =  ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_n_price, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(mValues.get(position).getAbsolute_date());
        DateFormat day = new SimpleDateFormat("EEEE");


        holder.date.setText(String.format("%s %s", new SimpleDateFormat("MMM dd,", Locale.getDefault())
                .format(mValues.get(position).getAbsolute_date()),
              day.format(mValues.get(position).getAbsolute_date())));
        int l =mValues.get(position).getDay_price();
        holder.price.setText(MessageFormat.format("Shs:{0}", String.valueOf(l)));
        holder.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listerner.onNext(mValues.get(position),mValues.get(position).getDay_price());
            }
        });

    }
    public void  setmValues(List<Days> days){
        mValues = days;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView date;
        public final TextView price,go;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            date= view.findViewById(R.id.date_tv);
            price = view.findViewById(R.id.price);
            go = view.findViewById(R.id.go);

        }


    }
}
interface goClick{
    void onNext(Days days, int price);
}