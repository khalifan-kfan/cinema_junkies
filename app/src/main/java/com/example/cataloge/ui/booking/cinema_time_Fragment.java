package com.example.cataloge.ui.booking;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cataloge.R;
import com.example.cataloge.ui.MTN.FixedValues;
import com.example.cataloge.ui.booking.data.CinemaHall;
import com.example.cataloge.ui.booking.data.MovieTimes;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class cinema_time_Fragment extends Fragment implements toBookingSeats,moreORless{
    private SharedViewmodel mViewModel;
    private Cinena_time_adaptor adapter;
    private NavController move;
   private RecyclerView time_rv;//invisible till expand button is clicked
    private MuviTymAdaptor adp;
    Boolean open = false;
    String interfaceid;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cinema_time__fragment, container, false);
        time_rv= v.findViewById(R.id.times_rv);
        time_rv.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adp=new MuviTymAdaptor();
        move = Navigation.findNavController(requireActivity(),R.id.fragment);
        //set up adaptor
        adapter =  new Cinena_time_adaptor(this);
        RecyclerView cinemas =v.findViewById(R.id.list_cinema);
        cinemas.setLayoutManager(new LinearLayoutManager(container.getContext(),RecyclerView.HORIZONTAL,false));
        cinemas.setAdapter(adapter);
        return v;
    }
    void FeelAdp(){
        mViewModel.getCinemaTimes(interfaceid).observe(requireActivity(), new Observer<List<MovieTimes>>() {
            @Override
            public void onChanged(List<MovieTimes> movieTimes) {
                adp.setTimesList(movieTimes);
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel= new ViewModelProvider(requireActivity()).get(SharedViewmodel.class);
      //
        mViewModel.getCinemas(mViewModel.getMovieID().getValue(), mViewModel.getDay_id()
                .getValue()).observe(requireActivity(), new Observer<List<CinemaHall>>() {
            @Override
            public void onChanged(List<CinemaHall> cinemaHalls) {
                adapter.setData(cinemaHalls);
            }
        });
    }
    @Override
    public void next(MovieTimes times_id, String cinema_id, String name) {
        //post day id
       // Toast.makeText(getContext(),String.valueOf(times_id.getAvailabe_seats().get(0)),Toast.LENGTH_SHORT).show();

        mViewModel.setCinema_id(cinema_id);
        mViewModel.setTime_id(times_id);
        mViewModel.Add_to_Selection(FixedValues.cinema,name);//11
        move.navigate(R.id.action_navigation_cine_times_to_navigation_seats);
    }


    @Override
    public void openOrclose(String id,String name) {
        if(id==null){
            //close times
            open=false;
            time_rv.setVisibility(View.GONE);
            adp.notifyItemRangeRemoved(0,adp.getItemCount());
        }else{
            //open times
            interfaceid=id;
            open=true;
            mViewModel.init4(id);
            time_rv.setVisibility(View.VISIBLE);
            if (adp.getItemCount() != 0) {
                adp.notifyItemRangeRemoved(0, adp.getItemCount());
            }
            adp = new MuviTymAdaptor(this,id,name);
            FeelAdp();
            time_rv.setAdapter(adp);

        }
    }
}
class MuviTymAdaptor extends  RecyclerView.Adapter<MuviTymAdaptor.ViewHolder>{
    List<MovieTimes> timesList = new ArrayList<>();
    cinema_time_Fragment c;
    toBookingSeats listener;
    String cinema_id;
    String name;
    MuviTymAdaptor(){ }
    public MuviTymAdaptor(cinema_time_Fragment c, String cinema_id, String name) {
        this.c= c;
        listener =  c;
        this.cinema_id= cinema_id;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_list,parent, false);
        return new ViewHolder(view);
    }
   // available_seats

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat tym = new SimpleDateFormat("HH:mm:");
        if(timesList.get(position).getAvailabe_seats().size()<1){
            //cant procced if no available seats
            holder.seats.setEnabled(false);
        }
        holder.time.setText(MessageFormat.format("{0} {1}-seats available",
                tym.format(timesList.get(position).getMovie_time()),
                String.valueOf(timesList.get(position).getAvailabe_seats().size())));

        holder.seats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.next(timesList.get(position),cinema_id,name);
            }
        });
    }

    public void setTimesList(List<MovieTimes> timesList) {
        this.timesList = timesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(timesList == null){
            return 0;
        }else return timesList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        Button seats;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time_);
            seats = itemView.findViewById(R.id.button);

        }
    }
}
interface toBookingSeats{
    void next(MovieTimes times_id, String cinema_id, String name);
}