package com.example.cataloge.ui.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cataloge.R;
import com.example.cataloge.ui.MTN.FixedValues;
import com.example.cataloge.ui.dashboard.data.Ticket;
import com.example.cataloge.ui.loginregister.LoginActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment implements ticketUse {

    private DashboardViewModel dashboardViewModel;
    TicketAdaptor my_ticket;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.sign_in);
        textView.setEnabled(false);
        final RecyclerView ticketList= root.findViewById(R.id.ticket_list);
        ticketList.setLayoutManager(new LinearLayoutManager(container.getContext(),RecyclerView.HORIZONTAL,false));
        dashboardViewModel.getLoggedUser().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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
        dashboardViewModel.getMyTicket().observe(getViewLifecycleOwner(), new Observer<List<Ticket>>() {
            @Override
            public void onChanged(List<Ticket> tickets) {
                if(tickets==null||tickets.isEmpty()){
                    Toast.makeText(container.getContext(),"You either havent logged in or have no " +
                            "tickets yet",Toast.LENGTH_LONG).show();
                }else {
                    my_ticket = new TicketAdaptor(DashboardFragment.this,tickets);
                  ticketList.setVisibility(View.VISIBLE);
                  ticketList.setAdapter(my_ticket);
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

    @Override
    public void tickettoHistory(Ticket ticket) {
        dashboardViewModel.sendToHistory(ticket);

    }
}

class TicketAdaptor extends RecyclerView.Adapter<TicketAdaptor.ViewHolder>{
    List<Ticket> tickets;
    Context ctx;
    ticketUse listener;

    public TicketAdaptor(DashboardFragment context, List<Ticket> tickets) {
        this.ctx=context.getContext();
        this.tickets=tickets;
        listener = context;
    }
    @NonNull
    @Override
    public TicketAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket,parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TicketAdaptor.ViewHolder holder, int position) {
        String list="numbers: .";
        for(int i =0;i<tickets.get(position).getSeats().size();i++){
            list+= String.valueOf(tickets.get(position).getSeats().get(i))+" , ";
        }
        holder.Seat_numbers.setText(list);
        holder.seat.setText(String.valueOf(tickets.get(position).getSeats().size())+"seats");
                Glide
                .with(ctx)
                .load(tickets.get(position).getPic_link())
                .centerCrop()
                .placeholder(R.drawable.movie_placeholder)
                .into(holder.thumbnail);
                holder.dif.setText(tickets.get(position).getDefinition());
                holder.title.setText(tickets.get(position).getTitle());
                holder.email.setText(tickets.get(position).getUsername());
                holder.cinema.setText(tickets.get(position).getCinema());
                holder.amount.setText(String.valueOf(tickets.get(position).getAmount()));

        DateFormat day = new SimpleDateFormat("EE");
        holder.date.setText(String.format("%s %s", new SimpleDateFormat("MMM dd,", Locale.getDefault())
                .format(tickets.get(position).getDate()), day.format(tickets.get(position).getDate())));
        holder.time.setText( new SimpleDateFormat("HH:mm",Locale.getDefault())
                .format(tickets.get(position).getTime()));
        //comparing current date to given date
        Date today = new Date();
        //if today is after yesterday or tomorrow
        //also adding one day to the date before expiry just to be sure ticket is really expired
        Calendar c = Calendar.getInstance();
        c.setTime(tickets.get(position).getTime());
        c.add(Calendar.DATE,1);
        if(today.after(c.getTime())){
         //ticket is one day expired hence removed
            holder.statet.setText(R.string.expired);
            holder.state.setVisibility(View.GONE);
            holder.cancel.setVisibility(View.VISIBLE);
            holder.cancel.setEnabled(true);
            holder.used.setEnabled(false);
            holder.used.setVisibility(View.GONE);
            holder.info.setText(R.string.take_off);

        }else {
            holder.statet.setText(R.string.On);
            holder.cancel.setEnabled(false);
        }

       holder.used.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Dialog

               AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
               builder.setTitle("Use This Ticket");
               builder.setMessage("This ticket will be considered used if you proceed, " +
                       "The cinema hall agent should click yes to confirm you have used it.  ");
               builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                       listener.tickettoHistory(tickets.get(position));

                    //  notifyItemRemoved(position);
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
           }
       });
       holder.cancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               listener.tickettoHistory(tickets.get(position));
               holder.remove(position);
           }
       });
    }



    @Override
    public int getItemCount() {
        return tickets.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail,state;
        TextView title,cinema,dif,date,time,Seat_numbers,seat,amount,email,statet,info;
        ImageButton used,cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.imageView);
            title= itemView.findViewById(R.id.title_);
            cinema= itemView.findViewById(R.id.cinema);
            dif = itemView.findViewById(R.id.dif);
            date= itemView.findViewById(R.id.date_);
            time = itemView.findViewById(R.id.time_tic);
            Seat_numbers=itemView.findViewById(R.id.seats_list);
            seat=itemView.findViewById(R.id.No_ofSeats);
            amount=itemView.findViewById(R.id.total);
            email=itemView.findViewById(R.id.email);
            used=itemView.findViewById(R.id.imageButton);
            state=itemView.findViewById(R.id.imageView2);
            statet = itemView.findViewById(R.id.state);
            cancel= itemView.findViewById(R.id.imageButton2);
            info= itemView.findViewById(R.id.textView15);
        }
        private void remove(int position) {
            tickets.remove(position);
            notifyItemRemoved(position);
        }

    }
}
interface ticketUse{
    void tickettoHistory(Ticket ticket);
}