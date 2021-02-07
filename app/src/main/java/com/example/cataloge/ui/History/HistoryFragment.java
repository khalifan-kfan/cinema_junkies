package com.example.cataloge.ui.History;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cataloge.R;
import com.example.cataloge.ui.dashboard.DashboardFragment;

import com.example.cataloge.ui.dashboard.data.Ticket;
import com.example.cataloge.ui.loginregister.LoginActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryFragment extends Fragment {
    private HistoryViewModel histViewModel;
    HistoryAdp history;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        histViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.history_frag, container, false);
        final TextView textView = root.findViewById(R.id.text_sign);
        textView.setEnabled(false);
        final RecyclerView ticketList= root.findViewById(R.id.history_list);
        ticketList.setLayoutManager(new LinearLayoutManager(container.getContext()));

        history = new HistoryAdp(HistoryFragment.this);
        ticketList.setVisibility(View.VISIBLE);
        ticketList.setAdapter(history);
        histViewModel.getLoggedUser().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean) {
                    textView.setVisibility(View.VISIBLE);
                    ticketList.setVisibility(View.GONE);
                    textView.setEnabled(true);
                    textView.setEnabled(true);
                    textView.setText(R.string.backToLogin);
                }
            }
        });
        histViewModel.getMyHTicket().observe(getViewLifecycleOwner(), new Observer<List<Ticket>>() {
            @Override
            public void onChanged(List<Ticket> tickets) {
                if(tickets==null||tickets.isEmpty()){
                    Toast.makeText(container.getContext(),"You either havent logged in or have no " +
                            "tickets yet",Toast.LENGTH_LONG).show();
                }else {
                    history.change(tickets);
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(container.getContext(), LoginActivity.class);
                startActivity(login);
            }
        });
        return root;
    }

}


class HistoryAdp extends RecyclerView.Adapter<HistoryAdp.ViewHolder>{
    List<Ticket> tickets = new ArrayList<>();
    Context ctx;

    public HistoryAdp(HistoryFragment historyFragment) {
        this.ctx=historyFragment.getContext();

    }

    @NonNull
    @Override
    public HistoryAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdp.ViewHolder holder, int position) {

        Glide
                .with(ctx)
                .load(tickets.get(position).getPic_link())
                .centerCrop()
                .placeholder(R.drawable.movie_placeholder)
                .into(holder.pic);
        holder.title.setText(tickets.get(position).getTitle());
        holder.amount.setText(String.valueOf(tickets.get(position).getAmount()));

        DateFormat day = new SimpleDateFormat("EE");
        holder.date.setText(String.format("%s %s", new SimpleDateFormat("MMM dd,", Locale.getDefault())
                .format(tickets.get(position).getDate()), day.format(tickets.get(position).getDate())));
        holder.time.setText( new SimpleDateFormat("HH:mm",Locale.getDefault()).format(tickets.get(position).getTime()));
    }
    @Override
    public int getItemCount() {
        return tickets.size();
    }
    public void change(List<Ticket> tickets){
        this.tickets = tickets;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView title,date,time,amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic=itemView.findViewById(R.id.movie_hist);
            title= itemView.findViewById(R.id.title_hist);
            time= itemView.findViewById(R.id.time_hist);
            amount = itemView.findViewById(R.id.amount_hist);
            date= itemView.findViewById(R.id.date_hist);
        }
    }
}