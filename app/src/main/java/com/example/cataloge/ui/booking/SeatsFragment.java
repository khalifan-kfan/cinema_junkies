package com.example.cataloge.ui.booking;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cataloge.R;
import com.example.cataloge.ui.MTN.FixedValues;
import com.example.cataloge.ui.MTN.MtnPayActivities;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SeatsFragment extends Fragment implements selectedlistener{

  List<Integer> fulllist;
   int amount;
   SharedViewmodel sharedViewmodel;
   SharedViewmodelFactory factory;
   seatsadapter adp;
    TextView price;
    Button to_Momo;
   List<Integer> seatsA = new ArrayList<>();
    ArrayList<Integer> selected = new ArrayList<>();
    private HashMap<String ,Object > Information;

    public SeatsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_seats, container, false);


        fulllist =new ArrayList<>();
        for (int i = 1;i<51;i++){
            fulllist.add(i);
        }
        RecyclerView allSeats =v.findViewById(R.id.list_seats);
        price = v.findViewById(R.id.amount_T);
        to_Momo = v.findViewById(R.id.mtnButton);
        price.setText(R.string.money);


        adp = new seatsadapter( fulllist,this);
        allSeats.setLayoutManager(new GridLayoutManager(v.getContext(),10));

        allSeats.setAdapter(adp);


        to_Momo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if list is not empty go to momo pay ativity
                // your taking selected seat
               
                if(!selected.isEmpty()) {
                    if (!(Information.size() < 13)) {
                        Intent to_pay = new Intent(container.getContext(), MtnPayActivities.class);
                        to_pay.putExtra("Information", Information);
                        container.getContext().startActivity(to_pay);
                        getActivity().finish();
                    } else {
                        Log.e("seats fragment", "onClick: to pay List empty");
                        Toast.makeText(container.getContext(), "Something is wrong with your " +
                                "selection please go back", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(container.getContext(),"You have no selection",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedViewmodel= new ViewModelProvider(requireActivity())
                .get(SharedViewmodel.class);

        sharedViewmodel.getPrice().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                amount = integer;
            }
        });
        sharedViewmodel.getSelection().observe(requireActivity(), new Observer<HashMap<String, Object>>() {
            @Override
            public void onChanged(HashMap<String, Object> stringObjectHashMap) {
                Information = new HashMap<>();
                Information = stringObjectHashMap;
            }
        });

        sharedViewmodel.getSeats().observe(requireActivity(), new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> integers) {

                adp.setAvailable(integers,fulllist);
            }
        });

    }

    @Override
    public void add_seat(int number) {
        selected.add(number);
        price.setText(MessageFormat.format("Shs{0} so far", String.valueOf(amount * selected.size())));

        sharedViewmodel.Add_to_Selection(FixedValues.seats,selected);//12
        sharedViewmodel.Add_to_Selection(FixedValues.amount,amount*selected.size());//13
    }

    @Override
    public void remove_seat(int number) {
        selected.remove(new Integer(number));
        price.setText(MessageFormat.format("Shs{0} so far", String.valueOf(amount * selected.size())));

        sharedViewmodel.Add_to_Selection(FixedValues.seats,selected);//12
        sharedViewmodel.Add_to_Selection(FixedValues.amount,amount*selected.size());//13
    }
}

class seatsadapter extends  RecyclerView.Adapter<seatsadapter.ViewHolder>{
    List<Integer> allseats;
    List<Long> available = new ArrayList<>();
    SeatsFragment ctx;
    selectedlistener update;
    public seatsadapter(List<Integer> fulllist, SeatsFragment context) {
        this.allseats=fulllist;
        ctx=context;
        update=context;
    }
    @NonNull
    @Override
    public seatsadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seat_, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // make colors different for active and inactive
       //to easily identify the seats available
        if(!available.isEmpty()) {
            for (int k = 0; k < available.size(); k++) {

                    if ((available.get(k)).intValue()==(allseats.get(position))) {
                    holder.vi.setEnabled(true);
                    holder.number.setBackgroundResource(R.color.teal_200);
                    holder.number.setText(String.valueOf(allseats.get(position)));
                    break;
                } else if (k == (available.size() - 1)) {
                    holder.vi.setEnabled(false);
                    holder.number.setText("X");
                }
            }
        }else {
            holder.vi.setEnabled(false);
            holder.number.setText("X");
        }

        holder.vi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(holder.number.getText().toString().toLowerCase().equals("x"))){
                    if ((holder.number.getText().toString().toLowerCase().equals("y"))) {
                        holder.number.setText(String.valueOf(allseats.get(position)));
                        //remove seat
                        update.remove_seat(allseats.get(position).intValue());
                    } else {
                        holder.number.setText("Y");
                        //add seat number
                        update.add_seat(allseats.get(position));
                    }
                }else {
                    Toast.makeText(ctx.getContext(),"seat Taken",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setAvailable(List<Long> seats,List<Integer> all){
        this.available = seats;
        this.allseats = all;
        notifyDataSetChanged();
      // in case a new list is imputted update view
      

    }
    @Override
    public int getItemCount() {
        return allseats.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        LinearLayout vi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            vi = itemView.findViewById(R.id.view3);
        }
    }
}
interface selectedlistener{
  void  add_seat(int number);
  void remove_seat(int number);
}
